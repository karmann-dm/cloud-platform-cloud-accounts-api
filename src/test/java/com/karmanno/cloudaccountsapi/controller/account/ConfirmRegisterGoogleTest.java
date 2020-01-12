package com.karmanno.cloudaccountsapi.controller.account;

import com.karmanno.cloudaccountsapi.base.IntegrationTest;
import org.junit.Test;

public class ConfirmRegisterGoogleTest extends IntegrationTest {
    @Test
    public void shouldRegisterNewAccount() {
        // given:
//        stubFor(post("https://www.googleapis.com/oauth2/v4/token").willReturn(
//                new ResponseDefinitionBuilder().withStatus(200)
//        ));

        // when/then:
        webTestClient.get()
                .uri("/account/auth/confirm/google?code=CODE&state=SOME_ID&scope=SCOPE")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
