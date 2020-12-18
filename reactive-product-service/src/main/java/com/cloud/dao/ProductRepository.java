package com.cloud.dao;

import com.cloud.pojo.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
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

    @Query("SELECT * FROM t_product WHERE category = :category limit :page,:pageSize")
    Flux<Product> findByCategory(String category, int page, int pageSize);

    @Query("select id,name,price,stock,category from t_product")
    Flux<Product> findAllByPage();
}
