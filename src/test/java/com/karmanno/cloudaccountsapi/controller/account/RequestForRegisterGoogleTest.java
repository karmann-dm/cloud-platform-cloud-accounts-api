package com.karmanno.cloudaccountsapi.controller.account;

import com.karmanno.cloudaccountsapi.base.IntegrationTest;
import com.karmanno.cloudaccountsapi.dto.AccountRequest;
import org.junit.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class RequestForRegisterGoogleTest extends IntegrationTest {

    @Test
    public void shouldGet4xxErrorWithEmptyRequest() {
        webTestClient.post()
                .uri("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.empty(), AccountRequest.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetLinkWithCorrectRequest() {
        webTestClient.post()
                .uri("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                        new AccountRequest("google", "SOME_ID")
                ), AccountRequest.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.url").isEqualTo("https://accounts.google.com/o/oauth2/v2/auth?" +
                        "response_type=code&" +
                        "redirect_uri=http://localhost:8080/account/confirm/google&" +
                        "scope=https://www.googleapis.com/auth/drive&" +
                        "state=SOME_ID&" +
                        "access_type=offline&" +
                        "client_id=212682554544-9tvsgg7bae0k980pq0d6nfuke77tc0pi.apps.googleusercontent.com");
    }
}
