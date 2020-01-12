package com.karmanno.cloudaccountsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAccountRequest implements AccountRequest {
    private String userId;
    private String code;
    private AccountStatus accountStatus;

    @Override
    public AccountType getType() {
        return AccountType.GOOGLE_DRIVE;
    }

    @Override
    public AccountStatus getStatus() {
        return accountStatus;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
