package com.karmanno.cloudaccountsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCloudAccount implements CloudAccount {
    private String userId;
    private GoogleTokenInfo token;
    private GoogleUserInfo userInfo;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public GoogleCloudAccount(String userId,
                              GoogleTokenInfo token,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
        this.userId = userId;
        this.token = token;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public AccountType getType() {
        return AccountType.GOOGLE_DRIVE;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
