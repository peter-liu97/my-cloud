package com.mycloud.config;

import org.elasticsearch.action.index.IndexResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveElasticsearchRepositories
public class EsConfig extends AbstractReactiveElasticsearchConfiguration {

    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        return ReactiveRestClients.create(ClientConfiguration.builder().connectedTo("121.199.65.11:9200").build());
    }
}



//public class EsConfig {
//    @Bean
//    public ReactiveElasticsearchClient client() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("121.199.65.11:9200")
//                .withWebClientConfigurer(webClient -> {
//                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                            .codecs(configurer -> configurer.defaultCodecs()
//                                    .maxInMemorySize(-1)
//                            )
//                            .build();
//                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
//                })
//                .build();
//
//        return ReactiveRestClients.create(clientConfiguration);
//    }
//

//    @Bean
//    @Primary
//    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("121.199.65.11:9200")
//                .withWebClientConfigurer(webClient -> {
//                    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                            .codecs(configurer -> configurer.defaultCodecs()
//                                    .maxInMemorySize(-1)
//                            )
//                            .build();
//                    return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
//                })
//                .build();
//        return ReactiveRestClients.create(clientConfiguration);
//    }
//    @Bean
//    public ElasticsearchConverter elasticsearchConverter() {
//        return new MappingElasticsearchConverter(elasticsearchMappingContext());
//    }
//    @Bean
//    public SimpleElasticsearchMappingContext elasticsearchMappingContext() {
//        return new SimpleElasticsearchMappingContext();
//    }
//    @Bean
//    public ReactiveElasticsearchOperations reactiveElasticsearchOperations() {
//        return new ReactiveElasticsearchTemplate(reactiveElasticsearchClient(), elasticsearchConverter());
//    }
//}
