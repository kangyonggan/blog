---
title: SpringBoot中RedisSession的使用
date: 2019-03-26 15:37:08
categories: Java后台
tags:
- Java
---

## 依赖
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.3.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

...

<spring-session-data-redis.version>2.1.3.RELEASE</spring-session-data-redis.version>
<spring-session.version>1.3.5.RELEASE</spring-session.version>

...

<!--spring-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>

<!--redis session-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
    <version>${spring-session-data-redis.version}</version>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
    <version>${spring-session.version}</version>
</dependency>
```

<!-- more -->

## 配置
`HttpSessionConfigurer.java`:

```
package com.kangyonggan.demo.configuration;

import com.kangyonggan.demo.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author kangyonggan
 * @since 2019/3/27 0027
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "demo:session")
public class HttpSessionConfigurer {

    /**
     * token放在http请求的header中，name=X-Auth-Token
     *
     * @return
     */
    @Bean
    public HeaderHttpSessionIdResolver httpSessionStrategy() {
        return new HeaderHttpSessionIdResolver(AppConstants.HEADER_TOKEN_NAME);
    }

}
```

其中`@EnableRedisHttpSession`注解用于启用redis session。

## 登录认证
```
/**
 * 判断是否登录
 *
 * @param response
 * @return
 */
private boolean isLogin(HttpServletResponse response) {
    // 判断是否登录
    if (!ParamsInterceptor.getSession().getId().equals(ParamsInterceptor.getToken())) {
        return false;
    }
    return true;
}

/**
 * 获取token
 *
 * @return
 */
public static String getToken() {
    return currentRequest.get().getHeader(AppConstants.HEADER_TOKEN_NAME);
}
```