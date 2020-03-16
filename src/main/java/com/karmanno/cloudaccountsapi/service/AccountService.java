package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.dto.AccountResponse;

public interface AccountService {

    String requestForRegister(String userId, String accountType);

    String register(String type, Object payload);

    AccountResponse getAccount(String type, String id);

}
