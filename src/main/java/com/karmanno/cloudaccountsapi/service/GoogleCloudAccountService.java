package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.*;
import com.karmanno.cloudaccountsapi.dto.GoogleConfirmRequest;
import com.karmanno.cloudaccountsapi.gateway.GoogleAuthApiGateway;
import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import com.karmanno.cloudaccountsapi.repository.AccountRequestRepository;
import com.karmanno.cloudaccountsapi.repository.CloudAccountRepository;
import com.karmanno.cloudaccountsapi.util.UriQueryConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudAccountService implements CloudAccountService {
    private final GoogleAuthApiGateway authApiGateway;
    private final AccountRequestRepository accountRequestRepository;
    private final CloudAccountRepository cloudAccountRepository;
    private final GoogleClientProperties googleClientProperties;
    private final UriQueryConverter uriQueryConverter;

    @Override
    public Mono<CloudAccount> register(Object payload) {
        String confirmRequestQuery = (String) payload;
        log.info("Confirmed request for Google account with payload {}", confirmRequestQuery);
        GoogleConfirmRequest confirmRequest = uriQueryConverter.convertGoogleRequest(confirmRequestQuery);
        return accountRequestRepository.save(
                new GoogleAccountRequest(
                        confirmRequest.getState(),
                        confirmRequest.getCode(),
                        AccountStatus.CONFIRMATION_SENT
                )
        )
                .map(googleAccountRequest -> authApiGateway.getToken(googleAccountRequest.getCode()))
                .flatMap(googleTokenResponse -> accountRequestRepository.findById(confirmRequest.getState())
                        .map(accountRequest -> (GoogleAccountRequest) accountRequest)
                        .map(googleAccountRequest -> googleAccountRequest.setAccountStatus(AccountStatus.CONFIRMED))
                        .flatMap(accountRequestRepository::save)
                        .map(savedRequest -> new GoogleCloudAccount(
                                confirmRequest.getState(),
                                googleTokenResponse
                        )))
                .flatMap(cloudAccountRepository::save);
    }

    @Override
    public AccountType getSupportableType() {
        return AccountType.GOOGLE_DRIVE;
    }

    @Override
    public Mono<String> authPage(String userId) {
        return Mono.just(generateAuthUrl(userId));
    }

    @Override
    public Mono<CloudAccount> getAccount(String id) {
        return Mono.empty();
    }

    private String generateAuthUrl(String userId) {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", String.format("http://localhost:8080/account/confirm/%s", "google"))
                .queryParam("scope", "https://www.googleapis.com/auth/drive")
                .queryParam("state", userId)
                .queryParam("access_type", "offline")
                .queryParam("client_id", googleClientProperties.getId())
                .build()
                .toUriString();
    }
}
