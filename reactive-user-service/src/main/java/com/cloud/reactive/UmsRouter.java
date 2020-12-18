package com.cloud.reactive;

import com.cloud.config.UmsRoutePath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UmsRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoute(UmsHandler umsHandler){
        return   RouterFunctions.route()
                .POST(UmsRoutePath.register,umsHandler::register)
                .build();
    }
}
