package com.cloud.reactive;


import com.cloud.pojo.UmsAdmin;
import com.cloud.security.AdminUserDetails;
import com.cloud.service.GateWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.swing.*;

@Component
public class GateWayHandler {

    @Autowired
    GateWayService gateWayService;

    public Mono<ServerResponse> fallback(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue(new Result("网络异常", 500));
    }
//    public Mono<ServerResponse> testLoadBalance(ServerRequest serverRequest) {
//        return  ServerResponse.ok().body(gateWayService.testLoadBalance("admin"), UserDetails.class);
//    }



    class Result {
        String mag;
        Integer code;

        public Result(String mag, Integer code) {
            this.mag = mag;
            this.code = code;
        }

        public String getMag() {
            return mag;
        }

        public void setMag(String mag) {
            this.mag = mag;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }


}
