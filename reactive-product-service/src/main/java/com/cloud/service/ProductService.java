package com.cloud.service;

import com.cloud.dao.ProductRepository;
import com.cloud.pojo.Product;
import com.cloud.pojo.ProductCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    /**
     * 保存商品
     * @param product
     * @return
     */
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 更新商品
     * @param product
     * @return
     */
    public Mono<Product> update(Product product) {
        return productRepository.existsById(product.getId()).flatMap(p -> {
            if (p) {
                return productRepository.save(product);
            }
            return Mono.just(product);
        });
    }

    /**
     * 查询商品
     * @param product
     * @return
     */
    public Mono<Product> findByProduct(Product product) {
        return productRepository.findById(product.getId());
    }

    /**
     * 根据商品ID查询商品
     * @param id
     * @return
     */
    public Mono<Product> findById(Integer id) {

        return productRepository.findById(id);
    }

    /**
     * 查询所有商品
     * @return
     */
    public Flux<Product> findByAll() {
        return productRepository.findAll();
    }

//    /**
//     * 查询所有商品（分页）
//     * @return
//     */
//    public Flux<Product> findAllByPage() {
//        return productRepository.findAllByPage();
//    }
//
//    /**
//     * 根据类别查询商品（分页）
//     * @return
//     */
//    public Flux<Product> findByCategoryPage(ProductCriteria productCriteria) {
//        return productRepository.findByCategory(productCriteria.getCategory(),(productCriteria.getPage()-1)*productCriteria.getPageSize(),productCriteria.getPageSize());
//    }

    /**
     *
     * @return
     */
    public Mono<Void> delete(Integer productId) {
        return productRepository.findById(productId).flatMap(p -> {
            return productRepository.delete(p);
        }).switchIfEmpty(Mono.empty());
    }

    public Mono<Void> deleteProduct(Integer productId) {
        return productRepository.existsById(productId).flatMap(p -> {
            if (p) {
                return productRepository.deleteById(productId);
            }
            return Mono.empty();
        });
    }

/*
    public Flux<Product> findByPage(ProductCriteria productCriteria) {
        return productDbClient.findByAllPage(productCriteria);
    }


*/

}
