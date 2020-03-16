package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.domain.CloudAccount;
import com.karmanno.cloudaccountsapi.domain.GoogleCreateAccountEvent;
import com.karmanno.cloudaccountsapi.dto.GoogleConfirmRequest;
import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import com.karmanno.cloudaccountsapi.repository.CloudAccountRepository;
import com.karmanno.cloudaccountsapi.util.UriQueryConverter;
import com.karmanno.cloudplatform.events.Event;
import com.karmanno.cloudplatform.events.EventPublishContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudAccountService implements CloudAccountService {
    private static final String RESPONSE_TYPE = "response_type";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String SCOPE = "scope";
    private static final String STATE = "state";
    private static final String ACCESS_TYPE = "access_type";
    private static final String CLIENT_ID = "client_id";

    private final CloudAccountRepository cloudAccountRepository;
    private final GoogleClientProperties googleClientProperties;
    private final UriQueryConverter uriQueryConverter;
    private final EventPublishContainer eventPublishContainer;
    private final EventIdGenerator eventIdGenerator;

    @Override
    public String register(Object payload) {
        String confirmRequestQuery = (String) payload;
        log.info("Confirmed request for Google account with payload {}", confirmRequestQuery);
        GoogleConfirmRequest confirmRequest = uriQueryConverter.convertGoogleRequest(confirmRequestQuery);

        Event event = new GoogleCreateAccountEvent(
                eventIdGenerator.generateId(),
                AccountType.GOOGLE_DRIVE,
                confirmRequest
        );
        eventPublishContainer.publishEvent(event);
        return event.getId();
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
        return UriComponentsBuilder.fromHttpUrl(googleClientProperties.getAuthUrl())
                .queryParam(RESPONSE_TYPE, googleClientProperties.getResponseType())
                .queryParam(REDIRECT_URI, googleClientProperties.getRedirectUri())
                .queryParam(SCOPE, googleClientProperties.getScope())
                .queryParam(STATE, userId)
                .queryParam(ACCESS_TYPE, googleClientProperties.getAccessType())
                .queryParam(CLIENT_ID, googleClientProperties.getId())
                .build()
                .toUriString();
    }
}
