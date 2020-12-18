package com.cloud.dao;

import com.cloud.pojo.UmsAdmin;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Component
public interface UmsAdminRepository  extends ReactiveCrudRepository<UmsAdmin,Integer> {

    @Query("select count(*) from ums_admin where userName = :userName limit 1")
    Mono<Integer> existsByUserName(String userName);

    @Query("select * from ums_admin where userName = :userName")
    Mono<UmsAdmin> findByUserName(String userName);
}
