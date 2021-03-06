package com.cloud.reactive;

import com.cloud.config.StockRoutePath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class  StockRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoute(StockHandler stockHandler){
        return   RouterFunctions.route()
                .GET(StockRoutePath.findStockByProductId,stockHandler::findStockByProductId)
                .build();

    }

}
