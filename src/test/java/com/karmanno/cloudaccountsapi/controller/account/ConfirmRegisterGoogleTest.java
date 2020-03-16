package com.karmanno.cloudaccountsapi.controller.account;

import com.karmanno.cloudaccountsapi.base.IntegrationTest;
import com.karmanno.cloudaccountsapi.service.EventIdGenerator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

public class ConfirmRegisterGoogleTest extends IntegrationTest {
    @MockBean
    private EventIdGenerator eventIdGenerator;

    @Test(expected = Exception.class)
    public void shouldThrowErrorWhenProvidingIncorrectAccountType() throws Exception {
        mockMvc.perform(
                get("/auth/confirm/soogle?code=CODE&state=STATE&scope=SCOPE")
        );
    }

    @Test
    public void should() throws Exception {
        when(eventIdGenerator.generateId()).thenReturn("RANDOM_UUID");

        mockMvc.perform(
                get("/auth/confirm/google?code=CODE&state=STATE&scope=SCOPE")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("RANDOM_UUID"));

        ConsumerRecord<String, String> received =
                records.poll(10, TimeUnit.SECONDS);

        assertThat(received, hasValue("greeting"));
        assertThat(received).has(key(null));
    }
}
