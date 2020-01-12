package com.karmanno.cloudaccountsapi.domain;

import java.util.Arrays;

public enum AccountType {
    GOOGLE_DRIVE("google");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public static AccountType ofName(String name) {
        return Arrays.stream(AccountType.values())
                .filter(val -> val.value.equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Unable to get AccountType with name = " + name));
    }
}
