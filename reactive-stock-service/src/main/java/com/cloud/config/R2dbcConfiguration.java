package com.cloud.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class R2dbcConfiguration {
//    @Bean
//    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
//        return (new R2dbcTransactionManager(connectionFactory));
//    }
//
//    @Bean
//    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
//        return TransactionalOperator.create(transactionManager);
//    }
}
