package com.mycloud.service;

import com.mycloud.dao.ReactivePersonRepository;
import com.mycloud.dao.ReactiveProductRepository;
import com.mycloud.pojo.Person;
import com.mycloud.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class ElasticsearchProductService {
//    @Autowired
//    private ReactiveElasticsearchClient reactiveElasticsearchClient;

    @Autowired
    ReactivePersonRepository reactivePersonRepository;

    @Autowired
    ReactiveProductRepository reactiveProductRepository;


//    private ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;
//
//    @PostConstruct
//    public void init() {
//        this.reactiveElasticsearchTemplate = new ReactiveElasticsearchTemplate(this.reactiveElasticsearchClient);
//    }

    public Mono<Product> saveProduct(Product product) {
        return reactiveProductRepository.save(product);
    }

    public Mono<Product> findById(Long id) {
        return reactiveProductRepository.findById(id);
    }

    public Flux<Product> findAll() {
        return reactiveProductRepository.findAll();

    }

}
