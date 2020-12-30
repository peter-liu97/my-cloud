package com.cloud.security;

import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

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

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
//                .csrf(csrf -> csrf.disable())) //禁用csrf
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))//将CSRF令牌存储在Cookie中
                .logout(logout -> logout.requiresLogout(new PathPatternParserServerWebExchangeMatcher("/logout")));
        return http.build();
    }
}
