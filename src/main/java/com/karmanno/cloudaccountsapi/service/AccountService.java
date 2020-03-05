package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<String> requestForRegister(String userId, String accountType);

    Mono<AccountResponse> register(String type, Object payload);

    Mono<AccountResponse> getAccount(String type, String id);

}
