package com.cloud.security;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.CommonResult;
import com.cloud.pojo.UmsPermission;
import com.cloud.service.AuthService;
import com.cloud.service.GateWayService;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.alibaba.nacos.client.utils.EnvUtil.LOGGER;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter implements GlobalFilter, Ordered {


    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    GateWayService gateWayService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthService authService;




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //2 检查token是否存在
        ServerHttpRequest request = exchange.getRequest();
        String jwtFromHeader = authService.getJwtFromHeader(request);
        RequestPath path = request.getPath();
        //安全路径,放行
        if(path.toString().equals("/admin/login")){
            return chain.filter(exchange);
        }

        if (StringUtils.isEmpty(jwtFromHeader)) {
            //拒绝访问 , 返回code 前端跳转登陆界面
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            byte[] bytes = "{\"status\":\"401\",\"msg\":\"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return  response.writeWith(Flux.just(buffer));
        }
        long expire = authService.getExpire(jwtFromHeader);
        if (expire > 0) {
            //权限校验
            String userName = jwtTokenUtil.getUserNameFromToken(jwtFromHeader);
            List<UmsPermission> umsPermission = gateWayService.getUmsPermission(userName);
            //校验通过
                    //校验不通过
            // 令牌续约
        }else {
            //重新登陆
        }
        return chain.filter(exchange);
}

    @Override
    public int getOrder() {
        return 0;
    }

}
