package com.cloud.reactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GateWayRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoute(GateWayHandler gateWayHandler){
        return   RouterFunctions.route()
                .POST("/fallback",gateWayHandler::fallback)
                .GET("/testLoadBalance",gateWayHandler::testLoadBalance)
                .build();
    }
}
