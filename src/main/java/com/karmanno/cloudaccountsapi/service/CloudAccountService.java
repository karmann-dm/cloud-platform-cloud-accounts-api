package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.domain.CloudAccount;

public interface CloudAccountService {

    String authPage(String userId);

    CloudAccount register(Object payload);

    CloudAccount getAccount(String id);

    AccountType getSupportableType();
}
