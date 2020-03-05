package com.karmanno.cloudaccountsapi.gateway;

import com.karmanno.cloudaccountsapi.client.GoogleApiClient;
import com.karmanno.cloudaccountsapi.dto.GoogleTokenResponse;
import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthApiGateway {
    private final GoogleApiClient googleApiClient;
    private final GoogleClientProperties googleClientProperties;

    /**
     *
     * EXAMPLE:
     *
     * POST /token HTTP/1.1
     * Host: oauth2.googleapis.com
     * Content-Type: application/x-www-form-urlencoded
     *
     * code=4/P7q7W91a-oMsCeLvIaQm6bTrgtp7&
     * client_id=your_client_id&
     * client_secret=your_client_secret&
     * redirect_uri=https%3A//oauth2.example.com/code&
     * grant_type=authorization_code
     *
     * @param code Code, received from completed auth page
     * @return Mono on which you can subscribe and receive body with Google OAuth token
     */
    public Mono<GoogleTokenResponse> getToken(String code) {
        return Mono.fromCallable(() -> googleApiClient.postForToken(
                new HashMap<String, String>() {{
                    put("code", code);
                    put("client_id", googleClientProperties.getId());
                    put("client_secret", googleClientProperties.getSecret());
                    put("redirect_uri", String.format("http://localhost:8080/account/confirm/%s", "google"));
                    put("grant_type", "authorization_code");
                }}
        )).doOnError(e -> log.error("Error while sending request for Google OAuth token", e));
    }

    /**
     * EXAMPLE:
     *
     * POST /token HTTP/1.1
     * Host: oauth2.googleapis.com
     * Content-Type: application/x-www-form-urlencoded
     *
     * client_id=your_client_id&
     * client_secret=your_client_secret&
     * refresh_token=refresh_token&
     * grant_type=refresh_token
     *
     * @return Mono on which you can subscribe and receive body with Google OAuth token
     */
    public Mono<GoogleTokenResponse> refreshToken(String refreshToken) {
        return Mono.fromCallable(() -> googleApiClient.postForToken(
                new HashMap<String, String>() {{
                    put("client_id", googleClientProperties.getId());
                    put("client_secret", googleClientProperties.getSecret());
                    put("refresh_token", refreshToken);
                    put("grant_type", "refresh_token");
                }}
        )).doOnError(e -> log.error("Error while refreshing Google OAuth token", e));
    }
}