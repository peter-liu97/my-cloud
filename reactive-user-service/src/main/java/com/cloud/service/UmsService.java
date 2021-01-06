package com.cloud.service;


import com.cloud.common.CommonPage;
import com.cloud.common.CommonResult;
import com.cloud.dao.UmsAdminDbClient;
import com.cloud.dao.UmsAdminRepository;
import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
import com.cloud.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UmsService {
    @Autowired
    private UmsAdminRepository umsAdminRepository;


    @Autowired
    private UmsAdminDbClient umsAdminDbClient;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Qualifier("passwordEncoder")
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Qualifier("reactiveRedisTemplate")
    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;


    public Mono<UmsAdmin> register(UmsAdmin umsAdmin) {
        return umsAdminRepository.existsByUserName(umsAdmin.getUsername())
                .flatMap(f -> {
                    if (f == 0) {
//                        umsAdmin.setCreateTime(new Date());
                        umsAdmin.setStatus(1);
                        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
                        return umsAdminRepository.save(umsAdmin);
                    }
                    return Mono.empty();
                });

    }

    public Mono<Boolean> existsByUserName(String userName) {
        return umsAdminRepository.existsByUserName(userName)
                .flatMap(f -> {
                    if (f == 0) {
                        return Mono.just(false);
                    }
                    return Mono.just(true);
                });
    }

    public Mono<CommonResult<Map<String, String>>> login(String username, String password) {
        return getAdminByUsername(username)
                .switchIfEmpty(Mono.empty())
                .flatMap(u -> {
                    if (!passwordEncoder.matches(password, u.getPassword())) {
                        return Mono.just(CommonResult.failed("用户名或密码错误"));
                    }
                    Map<String, String> tokenMap = new HashMap<>();
                    String token = jwtTokenUtil.generateToken(u.getUsername());
                    tokenMap.put("tokenHead", tokenHead);
                    tokenMap.put("token", token);
                    return reactiveRedisTemplate.opsForValue().set(token,u.getUsername(), Duration.ofSeconds(5 * 60))
                            .flatMap(p -> {
                                if (p) {
                                    return Mono.just(CommonResult.success(tokenMap));
                                }
                                return Mono.just(CommonResult.failed("连接超时,请重试"));
                            });
                });

    }


    public Mono<UmsAdmin> getAdminByUsername(String username) {
        return umsAdminRepository.findByUserName(username);
    }

    public Flux<UmsAdmin> findById(String id, Integer pageSize, Integer pageNum) {
        if (!id.isEmpty()) {
            return umsAdminRepository.findById(Integer.parseInt(id)).flux();
        }

        return umsAdminRepository.findAdmin(pageSize * pageNum - 1, pageNum);
    }

    public Flux<UmsPermission> getPermissionList(Long adminId) {
        return umsAdminDbClient.getPermissionList(adminId);
    }

    public Flux<UmsAdmin> findAll() {
        return umsAdminRepository.findAll();
    }
}
