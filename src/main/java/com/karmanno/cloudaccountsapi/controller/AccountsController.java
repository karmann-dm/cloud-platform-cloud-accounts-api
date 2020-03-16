package com.karmanno.cloudaccountsapi.controller;

import com.karmanno.cloudaccountsapi.dto.AccountRequest;
import com.karmanno.cloudaccountsapi.dto.AccountResponse;
import com.karmanno.cloudaccountsapi.dto.RedirectResponse;
import com.karmanno.cloudaccountsapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<RedirectResponse> requestForRegister(@RequestBody @Valid AccountRequest accountRequest) {
        String authRedirect = accountService.requestForRegister(
                accountRequest.getUserId(),
                accountRequest.getType()
        );
        return ResponseEntity.ok(new RedirectResponse(authRedirect));
    }

    @GetMapping("/auth/confirm/{type}")
    public ResponseEntity<String> confirmRegister(@PathVariable String type, HttpServletRequest request) {
        return ResponseEntity.ok(
                accountService
                        .register(type, request.getQueryString())
        );
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String type, @PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccount(type, id));
    }
}
