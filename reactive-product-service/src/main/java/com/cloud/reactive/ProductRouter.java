package com.cloud.reactive;

import com.cloud.config.RoutePath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class ProductRouter {


    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return  RouterFunctions.route()
                .POST(RoutePath.save,productHandler::save)
                .GET(RoutePath.find,productHandler::find)
                .DELETE(RoutePath.delete,productHandler::delete)
                .PUT(RoutePath.update,productHandler::update)
                .POST(RoutePath.saveMany,productHandler::saveMany)
                .GET(RoutePath.findStock,productHandler::findStock)
                .build();
    }

}
