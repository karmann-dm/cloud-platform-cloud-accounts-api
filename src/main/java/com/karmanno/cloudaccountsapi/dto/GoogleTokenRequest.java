package com.karmanno.cloudaccountsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleTokenRequest {
    @JsonProperty("code")
    private String code;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("grant_type")
    private String grantType;
}
