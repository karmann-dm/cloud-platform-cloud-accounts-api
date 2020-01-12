package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.domain.CloudAccount;
import reactor.core.publisher.Mono;

public interface CloudAccountService {
    Mono<String> authPage(String userId);
    Mono<CloudAccount> register(Object payload);
    Mono<CloudAccount> getAccount(String id);
    AccountType getSupportableType();
}
