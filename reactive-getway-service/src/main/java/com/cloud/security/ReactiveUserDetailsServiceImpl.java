package com.cloud.security;

import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Qualifier("loadBalancedUserAdminClientBuilder")
    @Autowired
    WebClient.Builder builder;

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        return builder.build()
                .get().uri("/admin/getByUserName/{userName}", userName)
                .retrieve().bodyToMono(UmsAdmin.class)
                .flatMap(user -> builder.build()
                        .get().uri("/admin/permission/{adminId}", user.getId())
                        .retrieve().bodyToFlux(UmsPermission.class)
                        .collectList()
                        .flatMap(list -> Mono.just(new AdminUserDetails(user, list))));

    }
}
