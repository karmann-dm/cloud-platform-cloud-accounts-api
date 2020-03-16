package com.karmanno.cloudaccountsapi.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventIdGenerator {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
