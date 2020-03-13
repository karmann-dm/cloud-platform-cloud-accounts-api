package com.karmanno.cloudaccountsapi.domain;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String userId;
    private String email;
    private String name;
}
