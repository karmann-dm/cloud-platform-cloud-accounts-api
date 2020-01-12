package com.karmanno.cloudaccountsapi.gateway;

import com.karmanno.cloudaccountsapi.client.GoogleApiClient;
import com.karmanno.cloudaccountsapi.dto.GoogleTokenResponse;
import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GoogleAuthApiGateway {
    private final GoogleApiClient googleApiClient;
    private final GoogleClientProperties googleClientProperties;

    public GoogleTokenResponse getToken(String code) {
        return googleApiClient.postForToken(
                new HashMap<String, String>() {{
                    put("code", code);
                    put("client_id", googleClientProperties.getId());
                    put("client_secret", googleClientProperties.getSecret());
                    put("redirect_uri", String.format("http://localhost:8080/account/confirm/%s", "google"));
                    put("grant_type", "authorization_code");
                }}
        );
    }
}