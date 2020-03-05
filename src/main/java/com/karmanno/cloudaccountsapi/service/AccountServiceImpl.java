package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final List<CloudAccountService> accountServices;

    @Override
    public Mono<String> requestForRegister(String userId, String accountType) {
        return findSuitableAccountService(accountType)
                .flatMap(service -> service.authPage(userId));
    }

    @Override
    public Mono<AccountResponse> register(String type, Object payload) {
        return findSuitableAccountService(type)
                .flatMap(service -> service.register(payload))
                .map(AccountResponse::new);
    }

    @Override
    public Mono<AccountResponse> getAccount(String type, String id) {
        return findSuitableAccountService(type)
                .flatMap(service -> service.getAccount(id))
                .map(AccountResponse::new);
    }

    private Mono<CloudAccountService> findSuitableAccountService(String accountType) {
        return Mono.just(
                accountServices.stream()
                        .filter(service -> service.getSupportableType().equals(
                                AccountType.ofName(accountType))
                        )
                        .findAny()
                        .orElseThrow(() ->
                                new RuntimeException("Unable to find service suitable for type " + accountType)
                        )
        );
    }
}
