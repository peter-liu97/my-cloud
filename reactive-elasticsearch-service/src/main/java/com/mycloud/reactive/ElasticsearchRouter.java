package com.mycloud.reactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ElasticsearchRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoute(ElasticsearchHandler elasticsearchHandler){
        return   RouterFunctions.route()
                .POST(ElasticsearchRoutePath.save,elasticsearchHandler::saveProduct)
                .POST(ElasticsearchRoutePath.findAllByPage,elasticsearchHandler::findAllByPage)
                .GET(ElasticsearchRoutePath.findById,elasticsearchHandler::findById)
                .build();
    }
}
