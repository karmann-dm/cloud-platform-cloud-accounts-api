package com.karmanno.cloudaccountsapi.domain;

import lombok.Data;

@Data
public class GoogleTokenInfo {
    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String tokenType;
}
