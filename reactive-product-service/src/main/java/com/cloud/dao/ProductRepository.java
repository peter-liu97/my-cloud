package com.cloud.dao;

import com.cloud.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @Description:
 * @Author: 伯乐
 * @Date: 2020/12/2 20:31
 */
@Repository
@Component
public interface ProductRepository extends ReactiveCrudRepository<Product,Integer> {


//    @Query("select id,name,price,stock,category from t_product")
//    Flux<Product> findAllByPage();

    /**
     * 'findAll()' in 'com.cloud.dao.ProductRepository' clashes with 'findAll()' in 'org.springframework.data.repository.reactive.ReactiveCrudRepository'; attempting to use incompatible return type
     * @return
     */
//    Flux<Product> findAll(Pageable pageable);

}
