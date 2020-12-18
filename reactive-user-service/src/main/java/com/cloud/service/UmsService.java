package com.cloud.service;


import com.cloud.dao.UmsAdminRepository;
import com.cloud.pojo.UmsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Service
public class UmsService {
    @Autowired
    private UmsAdminRepository umsAdminRepository;

    @Qualifier("passwordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<UmsAdmin> register(UmsAdmin umsAdmin) {
        return  umsAdminRepository.existsByUserName(umsAdmin.getUsername())
                .flatMap(f->{
                    if(f==0){
                        umsAdmin.setCreateTime(new Date());
                        umsAdmin.setStatus(1);
                        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
                        return  umsAdminRepository.save(umsAdmin);
                    }
                    return Mono.empty();
                });

    }

    public Mono<Boolean> existsByUserName(String userName){
        return umsAdminRepository.existsByUserName(userName)
                .flatMap(f->{
                    if(f==0){
                        return  Mono.just(false);
                    }
                    return Mono.just(true);
                });
    }

    public Mono<UmsAdmin> login(String username, String password) {

        return getAdminByUsername(username)
                .switchIfEmpty(Mono.empty())
                .flatMap(u->{
                    if (!passwordEncoder.matches(password,u.getPassword())) {
                        return Mono.empty();
                    }
                    return Mono.just(u);
                });

    }


    private Mono<UmsAdmin> getAdminByUsername(String username) {
        return umsAdminRepository.findByUserName(username);
    }
}
