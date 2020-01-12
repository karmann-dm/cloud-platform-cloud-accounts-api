package com.karmanno.cloudaccountsapi.dto;

import lombok.Data;

@Data
public class GoogleConfirmRequest {
    private String code;
    private String state;
    private String scope;
}
