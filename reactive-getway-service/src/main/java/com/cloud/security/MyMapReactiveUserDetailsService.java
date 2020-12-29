package com.cloud.security;

import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyMapReactiveUserDetailsService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService , ApplicationContextAware {

    @Qualifier("loadBalancedUserAdminClientBuilder")
    @Autowired
    WebClient.Builder builder;

    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    public MyMapReactiveUserDetailsService() {
    }

    @Override
    public Mono<UserDetails> updatePassword(UserDetails user, String newPassword) {
        return Mono.just(user).map((u) -> {
            return User.withUserDetails(u).password(newPassword).build();
        }).doOnNext((u) -> {
            String key = this.getKey(user.getUsername());
            this.users.put(key, u);
        });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        String key = this.getKey(username);
        UserDetails result = (UserDetails) this.users.get(key);
        return result == null ? null : Mono.just(User.withUserDetails(result).build());
    }

    public Mono<UserDetails> findByDB(String username) {
        return builder.build()
                .get().uri("/admin/getByUserName/{username}", username)
                .retrieve().bodyToMono(UmsAdmin.class)
                .flatMap(user -> {
                    if (user == null){
                        return Mono.empty();
                    }
                    return builder.build().get().uri("/admin/permission/{adminId}", user.getId())
                            .retrieve().bodyToFlux(UmsPermission.class)
                            .collectList()
                            .flatMap(list ->{
                                UserDetails userDetails =  new AdminUserDetails(user, list);
                                users.put(username,userDetails);
                                return Mono.just(userDetails);
                            });
                });
    }

    private String getKey(String username) {
        return username.toLowerCase();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.builder.build()
                .get().uri("/admin/findAll")
                .retrieve().bodyToFlux(UmsAdmin.class)
                .flatMap(user ->
                        builder.build().get().uri("/admin/permission/{adminId}", user.getId())
                                .retrieve().bodyToFlux(UmsPermission.class)
                                .collectList()
                                .flatMap(list -> Mono.just(new AdminUserDetails(user, list))))
                .collectList()
                .flatMap(p-> {
                    Iterator<AdminUserDetails> iterator = p.iterator();
                    if (iterator.hasNext()) {
                        AdminUserDetails next = iterator.next();
                        users.put(next.getUsername(), next);
                    }
                    System.out.println(users.size());
                    return null;
                });
    }
}
