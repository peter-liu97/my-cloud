package com.mycloud.reactive;


import com.mycloud.common.CommonPage;
import com.mycloud.common.PageInfo;
import com.mycloud.pojo.Product;
import com.mycloud.service.ElasticsearchProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Date;


@Component
public class ElasticsearchHandler {

    @Autowired
    ElasticsearchProductService elasticsearchProductService;


    public Mono<ServerResponse> saveProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(p -> {
                    p.setCreateTime(new Date());
                    return elasticsearchProductService.saveProduct(p);
                })
                .flatMap(p -> ServerResponse.ok().bodyValue(p));

    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        Long id = Long.parseLong(serverRequest.pathVariable("id"));
        return elasticsearchProductService.findById(id)

                .flatMap(p -> ServerResponse.ok().bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findAllByPage(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageInfo.class)
                .flatMap(p ->  elasticsearchProductService.findAll()
                            .collectList()
                        .flatMap(l-> ServerResponse.ok().bodyValue(CommonPage.restPage(l,p,0))));
    }


}
