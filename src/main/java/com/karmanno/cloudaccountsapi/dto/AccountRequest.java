package com.karmanno.cloudaccountsapi.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class AccountRequest {
    @NotEmpty
    private String type;

    @NotEmpty
    private String userId;
}
