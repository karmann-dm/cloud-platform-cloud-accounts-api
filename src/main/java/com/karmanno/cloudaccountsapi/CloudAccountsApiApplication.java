package com.karmanno.cloudaccountsapi;

import com.karmanno.cloudaccountsapi.properties.GoogleClientProperties;
import com.karmanno.cloudaccountsapi.properties.KafkaClientProperties;
import com.karmanno.cloudplatform.events.EventsProperties;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({
        GoogleClientProperties.class,
        KafkaClientProperties.class,
        EventsProperties.class
})
public class CloudAccountsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudAccountsApiApplication.class, args);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
