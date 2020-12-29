package com.cloud.security;

import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfigurer {

    @Qualifier("loadBalancedUserAdminClientBuilder")
    @Autowired
    WebClient.Builder builder;

    /**reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalStateException: executor not accepting a task
     Caused by: java.lang.IllegalStateException: executor not accepting a task
     at io.netty.resolver.AddressResolverGroup.getResolver(AddressResolverGroup.java:61) ~[netty-resolver-4.1.49.Final.jar:4.1.49.Final]
     Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException:
     Error has been observed at the following site(s):
     |_ checkpoint ⇢ Request to GET http://reative-user-service/admin/findAll [DefaultWebClient]
     Stack trace:
     *task
     * @return
     */

//    @Bean
    public  ReactiveUserDetailsService userDetailsService() {

        return (ReactiveUserDetailsService) builder.build()
                .get().uri("/admin/findAll")
                .retrieve().bodyToFlux(UmsAdmin.class)
                .flatMap(user ->
                        builder.build().get().uri("/admin/permission/{adminId}", user.getId())
                                .retrieve().bodyToFlux(UmsPermission.class)
                                .collectList()
                                .flatMap(list -> Mono.just(new AdminUserDetails(user, list))))
                .collectList()
                .flatMap(p->{
                    Map<String,UserDetails> adminUserDetails = new ConcurrentHashMap<>();
                    Iterator<AdminUserDetails> iterator = p.iterator();
                    if (iterator.hasNext()) {
                        AdminUserDetails next = iterator.next();
                        adminUserDetails.put(next.getUsername(),next);
                    }
                    return Mono.just(new MapReactiveUserDetailsService(adminUserDetails));
                }).subscribe();
    }

//    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                // Demonstrate that method security works
                // Best practice to use both for defense in depth
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .httpBasic(withDefaults())
                .build();
    }
}
