package com.myclod.reactive;

import com.alibaba.fastjson.JSON;
import com.myclod.pojo.City;
import com.myclod.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class RedisHandler {

    @Autowired
    private ReactiveStringRedisTemplate reactiveRedisTemplate;

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        String key = "person_" + id;
       return   reactiveRedisTemplate.opsForValue()
                .get(key)
               .flatMap(p->ServerResponse.ok().bodyValue(JSON.toJSON(p)));
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Person.class)
                .flatMap(person ->reactiveRedisTemplate.opsForValue()
                .set("person_"+person.getId(), JSON.toJSONString(person)))
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> hashSave(ServerRequest serverRequest) {
        return reactiveRedisTemplate.opsForHash()
                .put("宝龙地产","事业一部","成本部")
                .flatMap(p->ServerResponse.ok().bodyValue(JSON.toJSON(p)));
    }

    public Mono<ServerResponse> hashGet(ServerRequest serverRequest) {
        return ServerResponse.ok().body(reactiveRedisTemplate.opsForHash()
                .values("宝龙地产"), Map.class);
    }
}
