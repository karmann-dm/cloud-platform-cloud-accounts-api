package com.karmanno.cloudaccountsapi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaClientProperties {
    private String bootstrapServers;
    private KafkaProducerProperties producer;

    @Data
    public static class KafkaProducerProperties {
        private Class<?> keySerializer;
        private Class<?> valueSerializer;
    }
}
