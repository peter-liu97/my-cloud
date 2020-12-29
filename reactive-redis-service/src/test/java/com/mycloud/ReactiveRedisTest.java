package com.mycloud;

import com.myclod.RedisApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class ReactiveRedisTest {

    @Autowired
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Autowired
    ReactiveRedisConnectionFactory connectionFactory;
    private ReactiveRedisConnection connection;
    @Before
    public void setUp() {
        this.connection = connectionFactory.getReactiveConnection();
    }

    @Test
    public void test() {
        reactiveStringRedisTemplate.opsForValue()
                .set("peter34", "peter1");
    }


    @Test
    public void get() {
        reactiveStringRedisTemplate.opsForValue()
                .get("peter34")
                .subscribe(System.out::print);

    }


}
