package com.myclod.reactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RedisRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoute(RedisHandler redisHandler){
        return   RouterFunctions.route()
                .GET("/getById/{id}",redisHandler::getById)
                .POST("/save",redisHandler::save)
                .GET("/hashSave",redisHandler::hashSave)
                .GET("/hashGet",redisHandler::hashGet)
                .build();
    }
}
