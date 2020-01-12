package com.karmanno.cloudaccountsapi.base;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
public abstract class IntegrationTest {
    @Rule
    protected WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8888));

    @Autowired
    protected WebTestClient webTestClient;
}
