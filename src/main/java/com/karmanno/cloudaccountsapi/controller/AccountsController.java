package com.karmanno.cloudaccountsapi.controller;

import com.karmanno.cloudaccountsapi.dto.AccountRequest;
import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import com.karmanno.cloudaccountsapi.dto.RedirectResponse;
import com.karmanno.cloudaccountsapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;

    @PostMapping
    public Mono<?> requestForRegister(@RequestBody AccountRequest accountRequest) {
        return accountService.requestForRegister(
                accountRequest.getUserId(),
                accountRequest.getType()
        ).map(RedirectResponse::new);
    }

    @GetMapping("/auth/confirm/{type}")
    public Mono<AccountResponse> confirmRegister(@PathVariable String type, ServerHttpRequest request) {
        return accountService.register(type, request.getURI().getQuery());
    }

    @GetMapping("/{type}/{id}")
    public Mono<AccountResponse> getAccount(@PathVariable String type, @PathVariable String id) {
        return accountService.getAccount(type, id);
    }
}
