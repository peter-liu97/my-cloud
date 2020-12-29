package com.cloud.reactive;

import com.cloud.pojo.Product;
import com.cloud.pojo.ProductCriteria;
import com.cloud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    @Autowired
    private ProductService productService;
    @Autowired
    private TransactionalOperator transactionalOperator;

    @Qualifier("loadBalancedWebClientBuilder")
    @Autowired
    private WebClient.Builder  builder;


    public Mono<ServerResponse> findStock(ServerRequest serverRequest){
        var productId = Integer.parseInt(serverRequest.pathVariable("productId"));
        System.out.println("productId:"+productId);
       return builder.build()
               .get().uri("/route/stock/findStockByProductId/{productId}",productId)
               .retrieve().bodyToMono(Integer.class)
               .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    /**
     * 保存商品
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(i -> productService.save(i))
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    /**
     * 保存多个商品
     * @param serverRequest
     * @return
     */
    @Transactional
    public Mono<ServerResponse> saveMany(ServerRequest serverRequest) {
        return serverRequest.bodyToFlux(Product.class)
                .flatMap(i -> productService.save(i))
                .then(ServerResponse.ok().build())
                   .as(transactionalOperator::transactional);//手动使用事务
    }

    /**
     * 查询商品
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> find(ServerRequest serverRequest) {
        var productId = Integer.parseInt(serverRequest.pathVariable("productId"));
        return productService
                .findById(productId)
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }




    /**
     * 更新商品
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        var productId = Integer.parseInt(serverRequest.pathVariable("productId"));
        return productService
                .findById(productId)
                .flatMap(p -> serverRequest.bodyToMono(Product.class)
                        .flatMap(i -> productService.save(i)))
                .flatMap(p -> ServerResponse.ok().bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build())
                ;
    }


    /**
     * 删除商品
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        var productId = Integer.parseInt(serverRequest.pathVariable("productId"));
        return productService
                .findById(productId)
                .flatMap(p -> productService.delete(p.getId()).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
