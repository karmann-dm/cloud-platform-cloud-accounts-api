package com.karmanno.cloudaccountsapi.controller.account;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.karmanno.cloudaccountsapi.base.IntegrationTest;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ConfirmRegisterGoogleTest extends IntegrationTest {
    @Test
    public void shouldRegisterNewAccount() {
        // given:
        stubFor(post("https://www.googleapis.com/oauth2/v4/token").willReturn(
                new ResponseDefinitionBuilder().withStatus(200)
        ));

        // when/then:
        webTestClient.get()
                .uri("/account/auth/confirm/google?code=CODE&state=SOME_ID&scope=SCOPE")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
