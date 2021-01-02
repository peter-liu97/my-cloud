package com.cloud.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:例外资源
 * @Author: 伯乐
 * @Date: 2019/5/17 16:14
 */
//@Getter
//@Setter
//@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

    public String[] getUrls(){
        String[] urls = new String[this.urls.size()];
        for (int i = 0; i < this.urls.size(); i++) {
            urls[i] =  this.urls.get(i);
        }
        return urls;
    }

}
