package com.karmanno.cloudaccountsapi.repository;

import com.karmanno.cloudaccountsapi.domain.CloudAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudAccountRepository extends MongoRepository<CloudAccount, String> {
}
