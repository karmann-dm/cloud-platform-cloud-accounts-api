package com.karmanno.cloudaccountsapi.controller.account;

import com.karmanno.cloudaccountsapi.base.IntegrationTest;
import com.karmanno.cloudaccountsapi.dto.AccountRequest;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class RequestForRegisterGoogleTest extends IntegrationTest {

    @Test
    public void shouldGet4xxErrorWithEmptyRequest() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                    objectMapper
                            .writeValueAsBytes(
                                    new AccountRequest(null, null)
                            )
                )
        )
        .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetLinkWithCorrectRequest() throws Exception {
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(
                        objectMapper
                            .writeValueAsBytes(
                                    new AccountRequest("google", "SOME_ID")
                            )
                )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.url").value("https://accounts.google.com/o/oauth2/v2/auth?" +
                "response_type=code&" +
                "redirect_uri=http://${CLOUD_ACCOUNTS_API_HOST}/account/confirm/google&" +
                "scope=https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/userinfo.profile&" +
                "state=SOME_ID&" +
                "access_type=offline&" +
                "client_id=212682554544-9tvsgg7bae0k980pq0d6nfuke77tc0pi.apps.googleusercontent.com"));
    }
}
