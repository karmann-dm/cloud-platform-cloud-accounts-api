package com.karmanno.cloudaccountsapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public interface CloudAccount {
    @Id
    String getId();

    AccountType getType();
}
