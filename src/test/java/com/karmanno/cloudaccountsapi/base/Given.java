package com.karmanno.cloudaccountsapi.base;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Given {

    public static class GivenBuilder {
        private WireMockServer wireMockServer;
        private boolean withOAuthResponse = false;

        public Given please() {
            Given given = new Given();
            if (withOAuthResponse) {
                wireMockServer.stubFor(
                        post(urlEqualTo("/account"))
                            .willReturn(aResponse().withStatus(200))
                );
            }
            return given;
        }

        public GivenBuilder withOAuthResponse() {
            this.withOAuthResponse = true;
            return this;
        }

        private GivenBuilder(WireMockServer wireMockServer) {
            this.wireMockServer = wireMockServer;
        }
    }

    public static GivenBuilder giveMe(WireMockServer wireMockServer) {
        return new GivenBuilder(wireMockServer);
    }
}
