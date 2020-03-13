package com.karmanno.cloudaccountsapi.service;

import com.karmanno.cloudaccountsapi.domain.AccountType;
import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final List<CloudAccountService> accountServices;

    @Override
    public String requestForRegister(String userId, String accountType) {
        return findSuitableAccountService(accountType)
                .authPage(userId);
    }

    @Override
    public AccountResponse register(String type, Object payload) {
        return new AccountResponse(
                findSuitableAccountService(type)
                        .register(payload)
        );
    }

    @Override
    public AccountResponse getAccount(String type, String id) {
        return new AccountResponse(
                findSuitableAccountService(type)
                        .getAccount(id)
        );
    }

    private CloudAccountService findSuitableAccountService(String accountType) {
        return accountServices.stream()
                .filter(service -> service.getSupportableType().equals(
                        AccountType.ofName(accountType))
                )
                .findAny()
                .orElseThrow(() ->
                        new RuntimeException("Unable to find service suitable for type " + accountType)
                );
    }
}
