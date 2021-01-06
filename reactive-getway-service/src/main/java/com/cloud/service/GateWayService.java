package com.cloud.service;


import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GateWayService {
    @Qualifier("loadBalancedUserAdminClientBuilder")
    @Autowired
    WebClient.Builder builder;


    public UmsAdmin getUmsAdmin(String userName) {

        return builder.build()
                .get().uri("/admin/getByUserName/{userName}", userName)
                .retrieve().bodyToMono(UmsAdmin.class)
                .block();
    }

    public List<UmsPermission> getUmsPermission(String userName) {
        return builder.build().get().uri("/admin/getByUserName/{userName}", userName)
                .retrieve().bodyToMono(UmsAdmin.class)
                .flatMap(user ->builder.build().get().uri("/admin/permission/{adminId}", user.getId())
                            .retrieve().bodyToFlux(UmsPermission.class)
                            .collectList()).block();
    }
}
