package com.karmanno.cloudaccountsapi.scheduler;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.domain.GoogleCloudAccount;
import com.karmanno.cloudaccountsapi.gateway.GoogleAuthApiGateway;
import com.karmanno.cloudaccountsapi.repository.CloudAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {
    private final CloudAccountRepository cloudAccountRepository;
    private final GoogleAuthApiGateway googleAuthApiGateway;

    @Scheduled(cron = "0 */1 * * *")
    public void refreshGoogleTokens() {
        cloudAccountRepository.findAll()
                .filter(cloudAccount -> cloudAccount.getType().equals(AccountType.GOOGLE_DRIVE))
                .map(cloudAccount -> (GoogleCloudAccount) cloudAccount)
                .flatMap(googleCloudAccount -> {
                    long expiresIn = Long.parseLong(googleCloudAccount.getToken().getExpiresIn());
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime modifiedAt = googleCloudAccount.getModifiedAt();
                    long secondsAfterModified = modifiedAt.until(now, ChronoUnit.SECONDS);
                    if (secondsAfterModified >= expiresIn) {
                        return refreshAccount(googleCloudAccount);
                    }
                    else
                        return Flux.empty();
                })
                .subscribe();
    }

    Mono<GoogleCloudAccount> refreshAccount(GoogleCloudAccount googleCloudAccount) {
        return googleAuthApiGateway.refreshToken(googleCloudAccount.getToken().getRefreshToken())
                .map(googleTokenResponse -> {
                    googleCloudAccount.setToken(googleTokenResponse);
                    googleCloudAccount.setModifiedAt(LocalDateTime.now());
                    return googleCloudAccount;
                })
                .flatMap(cloudAccountRepository::save);
    }
}
