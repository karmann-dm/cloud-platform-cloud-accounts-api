package com.karmanno.cloudaccountsapi.client;

import com.karmanno.cloudaccountsapi.config.FeignClientConfiguration;
import com.karmanno.cloudaccountsapi.dto.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(
        value = "googleApiClient",
        url = "https://oauth2.googleapis.com",
        configuration = FeignClientConfiguration.class
)
public interface GoogleApiClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse postForToken(
            @RequestBody Map<String, ?> request
    );
}
