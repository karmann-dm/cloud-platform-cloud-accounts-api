package com.karmanno.cloudaccountsapi.repository;

import com.karmanno.cloudaccountsapi.domain.AccountRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRequestRepository extends ReactiveMongoRepository<AccountRequest, String> {
    Mono<AccountRequest> findByUserIdAndType(String userId, String type);
}
