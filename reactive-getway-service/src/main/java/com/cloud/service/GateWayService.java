package com.cloud.service;


import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GateWayService {
    @Qualifier("loadBalancedUserAdminClientBuilder")
    @Autowired
    WebClient.Builder builder;


//    public UserDetails getUserDetails(String userName) {
//
//        return builder.build()
//                .get().uri("/admin/getByUserName/{userName}", userName)
//                .retrieve().bodyToMono(UmsAdmin.class)
//                .flatMap(user -> {
//                    return builder.build().get().uri("/admin/permission/{adminId}", user.getId())
//                            .retrieve().bodyToFlux(UmsPermission.class)
//                            .collectList()
//                            .flatMap(list -> {
//                                return Mono.just(new AdminUserDetails(user, list));
//                            });
//                }).block();
//    }
}
