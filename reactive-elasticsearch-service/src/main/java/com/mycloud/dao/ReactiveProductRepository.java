package com.mycloud.dao;

import com.mycloud.pojo.Person;
import com.mycloud.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface ReactiveProductRepository extends ReactiveElasticsearchRepository<Product, Long> {
}
