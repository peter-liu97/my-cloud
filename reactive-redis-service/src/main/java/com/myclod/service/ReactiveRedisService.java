package com.myclod.service;


import com.alibaba.fastjson.JSON;
import com.myclod.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Service
public class ReactiveRedisService {
    @Qualifier("reactiveRedisTemplate")
    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;


    public Mono<Boolean> setString(String key, String value){
        return reactiveRedisTemplate.opsForValue().set(key,value, Duration.ofSeconds(20));
    }

    public Mono<Boolean> savePerson(Person person){
        return reactiveRedisTemplate.opsForHash().put("person",person.getId(), JSON.toJSONString(person));
    }

    public Mono<Object> getPerson(String id){
        return reactiveRedisTemplate.opsForHash().get("person",id)
                .switchIfEmpty(Mono.empty())
                .map(Object::toString)
                .map(JSON::parse);
    }

    public Mono<Boolean> setTtlNode(String key,String value,Long time){
        return reactiveRedisTemplate.opsForValue().set(key,value,Duration.ofSeconds(time));
    }

    public Mono<Long> getTtlTime(String key){
        return reactiveRedisTemplate.getExpire(key)
                .map(d ->d.get(ChronoUnit.SECONDS));
    }
    public Mono<Boolean> ttlRenew(String key,Long time){
        return reactiveRedisTemplate.expire(key, Duration.ofSeconds(time));
    }

}
