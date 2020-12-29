package com.mycloud.dao;

import com.mycloud.pojo.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface ReactivePersonRepository extends ReactiveElasticsearchRepository<Person, Long> {

    Flux<Person> findByFirstName(String firstname);

    Flux<Person> findByFirstNameLike(String firstname, Pageable pageable);
}
