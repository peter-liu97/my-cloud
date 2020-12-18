package com.cloud.reactive;

import com.cloud.config.UmsRoutePath;
import com.cloud.pojo.UmsAdmin;
import com.cloud.service.UmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UmsHandler {
    @Autowired
    private UmsService umsService;

    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UmsAdmin.class)
                .flatMap(i->umsService.register(i))
                .flatMap(p->ServerResponse.ok().bodyValue(p))
                .switchIfEmpty(ServerResponse.status(403).build());
    }

//    public Mono<ServerResponse> login(ServerRequest serverRequest){
//        return serverRequest.bodyToMono(UmsAdmin.class)
//                .flatMap(u->umsService.login(u.getUsername(),u.getPassword()))
////                .switchIfEmpty()
//    }




}
