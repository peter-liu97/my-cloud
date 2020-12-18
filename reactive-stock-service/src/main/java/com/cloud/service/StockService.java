package com.cloud.service;

import com.cloud.dao.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;


    public Mono<Integer> findStockByProductId(Long productId){
        return stockRepository.findStockByProductId(productId);
    }

}
