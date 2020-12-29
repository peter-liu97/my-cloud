package com.cloud.service;


import com.cloud.common.CommonPage;
import com.cloud.dao.UmsAdminRepository;
import com.cloud.pojo.UmsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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
//                        umsAdmin.setCreateTime(new Date());
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


    public Mono<UmsAdmin> getAdminByUsername(String username) {
        return umsAdminRepository.findByUserName(username);
    }

    public Flux<UmsAdmin> findById(String id, Integer pageSize, Integer pageNum) {
        if(!id.isEmpty()){
            return umsAdminRepository.findById(Integer.parseInt(id)).flux();
        }

        return umsAdminRepository.findAdmin(pageSize*pageNum-1,pageNum);
    }
}
