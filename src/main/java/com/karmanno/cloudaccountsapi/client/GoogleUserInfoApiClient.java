package com.karmanno.cloudaccountsapi.client;

import com.karmanno.cloudaccountsapi.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

//https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=youraccess_token
@FeignClient(
        value = "googleUserInfoApiClient",
        url = "https://www.googleapis.com/oauth2/v1",
        configuration = FeignClientConfiguration.class
)
public interface GoogleUserInfoApiClient {
}

