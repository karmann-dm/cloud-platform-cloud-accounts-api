package com.karmanno.cloudaccountsapi.domain;

import com.karmanno.cloudaccountsapi.dto.GoogleTokenResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCloudAccount implements CloudAccount {
    private String userId;
    private GoogleTokenResponse token;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public AccountType getType() {
        return AccountType.GOOGLE_DRIVE;
    }
}
