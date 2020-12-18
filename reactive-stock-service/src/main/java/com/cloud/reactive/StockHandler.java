package com.cloud.reactive;

import com.cloud.dao.StockRepository;
import com.cloud.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class  StockHandler {

    @Autowired
    private StockService stockService;

    public  Mono<ServerResponse> findStockByProductId(ServerRequest serverRequest) {
        var productId = Long.parseLong(serverRequest.pathVariable("productId"));
        return stockService.findStockByProductId(productId)
                .flatMap(p->ServerResponse.ok().bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
