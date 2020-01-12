package com.karmanno.cloudaccountsapi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.client")
@Data
public class GoogleClientProperties {
    private String id;
    private String secret;
}
