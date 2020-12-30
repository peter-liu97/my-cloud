package com.cloud.dao;

import com.cloud.pojo.UmsAdmin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface UmsAdminRepository  extends ReactiveCrudRepository<UmsAdmin,Integer> {

}
