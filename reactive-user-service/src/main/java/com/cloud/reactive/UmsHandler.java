package com.cloud.reactive;

import com.cloud.common.CommonResult;
import com.cloud.config.UmsRoutePath;
import com.cloud.pojo.UmsAdmin;
import com.cloud.pojo.UmsPermission;
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
                .flatMap(p->ServerResponse.ok().bodyValue(CommonResult.success(p)))
                .switchIfEmpty(ServerResponse.ok().bodyValue(CommonResult.failed()));
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest){
        return serverRequest.bodyToMono(UmsAdmin.class)
                .flatMap(u->umsService.login(u.getUsername(),u.getPassword()))
                .flatMap(p->ServerResponse.ok().bodyValue(p))
                .switchIfEmpty(ServerResponse.ok().bodyValue(CommonResult.failed("用户名不存在")));
    }


    public Mono<ServerResponse> findByUserName(ServerRequest serverRequest) {
        var userName = serverRequest.pathVariable("userName");
        return umsService.getAdminByUsername(userName)
                .flatMap(p->ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        var userName = serverRequest.pathVariable("userName");
        return umsService.getAdminByUsername(userName)
                .flatMap(p->ServerResponse.ok().bodyValue(CommonResult.success(p)))
                .switchIfEmpty(ServerResponse.ok().bodyValue(CommonResult.failed()));
    }

    public Mono<ServerResponse> getItem(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        var size = serverRequest.pathVariable("pageSize");
        var num = serverRequest.pathVariable("pageNum");
        var pageSize = Integer.parseInt(size.isEmpty()?"5":size);
        var pageNum = Integer.parseInt(num.isEmpty()?"1":num);
        return null;
    }

    public Mono<ServerResponse> getPermissionList(ServerRequest serverRequest) {
        Long adminId = Long.parseLong(serverRequest.pathVariable("adminId"));
        return ServerResponse.ok().body( umsService.getPermissionList(adminId),UmsPermission.class);
    }


    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok().body(umsService.findAll(),UmsAdmin.class);
    }
}
