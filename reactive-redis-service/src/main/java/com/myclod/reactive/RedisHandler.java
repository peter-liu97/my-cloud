package com.myclod.reactive;

import com.alibaba.fastjson.JSON;
import com.myclod.pojo.City;
import com.myclod.pojo.Person;
import com.myclod.pojo.TTLNode;
import com.myclod.service.ReactiveRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Qualifier("reactiveRedisTemplate")
    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Autowired
    private ReactiveRedisService reactiveRedisService;

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        String key = "person_" + id;
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .flatMap(p -> ServerResponse.ok().bodyValue(JSON.toJSON(p)));
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Person.class)
                .flatMap(person -> reactiveRedisTemplate.opsForValue()
                        .set("person_" + person.getId(), JSON.toJSONString(person)))
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> hashSave(ServerRequest serverRequest) {
        return reactiveRedisTemplate.opsForHash()
                .put("宝龙地产", "事业一部", "成本部")
                .flatMap(p -> ServerResponse.ok().bodyValue(JSON.toJSON(p)));
    }

    public Mono<ServerResponse> hashGet(ServerRequest serverRequest) {
        return ServerResponse.ok().body(reactiveRedisTemplate.opsForHash()
                .values("宝龙地产"), Map.class);
    }

    public Mono<ServerResponse> saveTTLNode(ServerRequest serverRequest) {
        return reactiveRedisTemplate.opsForValue().set("key1", "v1", 2)
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> getTTLNode(ServerRequest serverRequest) {
        return reactiveRedisTemplate.opsForValue().get("key3")
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> savekeyAndValue(ServerRequest serverRequest) {
        String key = serverRequest.pathVariable("key");
        String value = serverRequest.pathVariable("value");
        return reactiveRedisService.setString(key, value)
                .flatMap(p -> ServerResponse.ok().bodyValue(p));
    }

    public Mono<ServerResponse> savePerson(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Person.class).flatMap(
                person -> reactiveRedisService.savePerson(person)
                        .flatMap(p -> ServerResponse.ok().bodyValue(p)));
    }

    public Mono<ServerResponse> getPerson(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return reactiveRedisService.getPerson(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(p->ServerResponse.ok().bodyValue(p));
    }
    public Mono<ServerResponse> setTtlNode(ServerRequest serverRequest) {
       return serverRequest.bodyToMono(TTLNode.class).flatMap(t->
               reactiveRedisService.setTtlNode(t.getKey(), t.getValue(), t.getTime())
                   .flatMap(b -> ServerResponse.ok().bodyValue(b)));
    }
    public Mono<ServerResponse> getTtlTime(ServerRequest serverRequest) {
        String key = serverRequest.pathVariable("key");
       return reactiveRedisService.getTtlTime(key)
               .flatMap(p->ServerResponse.ok().bodyValue(p));
    }
    public Mono<ServerResponse> ttlRenew(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TTLNode.class).flatMap(t->
                reactiveRedisService.ttlRenew(t.getKey(),  t.getTime())
                        .flatMap(b -> ServerResponse.ok().bodyValue(b)));
    }
}
