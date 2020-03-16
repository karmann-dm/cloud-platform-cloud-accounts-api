package com.karmanno.cloudaccountsapi.domain;

import com.karmanno.cloudaccountsapi.dto.GoogleConfirmRequest;
import com.karmanno.cloudplatform.events.Event;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoogleCreateAccountEvent implements Event {
    private final String id;
    private final AccountType accountType;
    private final GoogleConfirmRequest confirmRequest;

    public GoogleCreateAccountEvent(String id,
                                    AccountType accountType,
                                    GoogleConfirmRequest confirmRequest) {
        this.id = id;
        this.accountType = accountType;
        this.confirmRequest = confirmRequest;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return LocalDateTime.now();
    }

    @Override
    public GoogleCreateAccountEvent getPayload() {
        return null;
    }
}
