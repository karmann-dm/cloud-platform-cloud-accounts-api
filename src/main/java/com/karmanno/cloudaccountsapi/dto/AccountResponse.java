package com.karmanno.cloudaccountsapi.dto;

import com.karmanno.cloudaccountsapi.domain.CloudAccount;
import lombok.Value;

@Value
public class AccountResponse {
    private CloudAccount cloudAccount;
}
