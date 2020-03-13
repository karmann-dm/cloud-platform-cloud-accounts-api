package com.karmanno.cloudaccountsapi.controller;

import com.karmanno.cloudaccountsapi.dto.AccountRequest;
import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import com.karmanno.cloudaccountsapi.dto.RedirectResponse;
import com.karmanno.cloudaccountsapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<RedirectResponse> requestForRegister(@RequestBody AccountRequest accountRequest) {
        String authRedirect = accountService.requestForRegister(
                accountRequest.getUserId(),
                accountRequest.getType()
        );
        return ResponseEntity.ok(new RedirectResponse(authRedirect));
    }

    @GetMapping("/auth/confirm/{type}")
    public ResponseEntity<AccountResponse> confirmRegister(@PathVariable String type, ServerHttpRequest request) {
        return ResponseEntity.ok(
                accountService
                        .register(type, request.getURI().getQuery())
        );
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String type, @PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccount(type, id));
    }
}
