package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.*;
import com.karmanno.cloudaccountsapi.dto.GoogleConfirmRequest;
import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import com.karmanno.cloudaccountsapi.repository.CloudAccountRepository;
import com.karmanno.cloudaccountsapi.util.UriQueryConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudAccountService implements CloudAccountService {
    private final CloudAccountRepository cloudAccountRepository;
    private final GoogleClientProperties googleClientProperties;
    private final UriQueryConverter uriQueryConverter;

    // TODO: Error handling
    @Override
    public CloudAccount register(Object payload) {
        String confirmRequestQuery = (String) payload;
        log.info("Confirmed request for Google account with payload {}", confirmRequestQuery);

        GoogleConfirmRequest confirmRequest = uriQueryConverter.convertGoogleRequest(confirmRequestQuery);

        /*
        Steps:
        1. Create request for account creation
        2. Delegate request creation to the cloud-platform-cloud-account-requests-api with event
        3. Catch event of successful request or error while performing the request with auth token
        4. Fetch user info by calling cloud-platform-cloud-accounts-user-info-api with event
        5. Catch event with user data
        6. Save account with auth token and user in repository and return all required data
         */

        return accountRequestRepository.save(
                new GoogleAccountRequest(
                        confirmRequest.getState(),
                        confirmRequest.getCode(),
                        AccountRequestStatus.CONFIRMATION_SENT
                )
        )
                .flatMap(request -> authApiGateway.getToken(request.getCode()))
                .flatMap(
                        googleTokenResponse ->
                                accountRequestRepository.findById(confirmRequest.getState()
                                )
                        .map(accountRequest -> (GoogleAccountRequest) accountRequest)
                        .map(
                                googleAccountRequest ->
                                        googleAccountRequest.setAccountRequestStatus(AccountRequestStatus.CONFIRMED)
                        )
                        .flatMap(accountRequestRepository::save)
                        .map(
                                savedRequest ->
                                        new GoogleCloudAccount(
                                            confirmRequest.getState(),
                                            googleTokenResponse,
                                            LocalDateTime.now(),
                                            LocalDateTime.now()
                                        )
                        )
                )
                .flatMap(googleCloudAccount -> accountInfoApiGateway.getUserInfo(
                        googleCloudAccount.getToken().getAccessToken()
                )
                .flatMap(googleUserInfoResponse -> {
                    googleCloudAccount.setUserInfo(googleUserInfoResponse);
                    return cloudAccountRepository.save(googleCloudAccount);
                }));
    }

    @Override
    public AccountType getSupportableType() {
        return AccountType.GOOGLE_DRIVE;
    }

    @Override
    public String authPage(String userId) {
        return generateAuthUrl(userId);
    }

    @Override
    public CloudAccount getAccount(String id) {
        return cloudAccountRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(String.format("Account with id = %s not found", id))
                );
    }

    private String generateAuthUrl(String userId) {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth") // TODO: Fix hardcoded values
                .queryParam("response_type", "code") // TODO: Always code?
                .queryParam("redirect_uri", String.format("http://localhost:8080/account/confirm/%s", "google")) // TODO: Fix hardcoded values
                .queryParam("scope", "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.profile") // TODO: Fix hardcoded valuse
                .queryParam("state", userId)
                .queryParam("access_type", "offline") // TODO: Always offline?
                .queryParam("client_id", googleClientProperties.getId())
                .build()
                .toUriString();
    }
}
