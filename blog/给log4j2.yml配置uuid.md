---
title: 给log4j2.yml配置uuid
date: 2019-04-15 10:16:08
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


<!--Log4j2 yml-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.9.8</version>
</dependency>
```

<!-- more -->

## log4j2.yml

```
Configuration:
  status: warn
  monitorInterval: 300

  Appenders:
    Console:
      name: STDOUT
      PatternLayout:
        pattern: "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] <%X{uuid}> - %msg%n"

  Loggers:
    Root:
      level: info
      additivity: true
      AppenderRef:
        - ref: STDOUT


```

## MvcConfigure.java

```
package com.kangyonggan.demo.configuration;

import com.htsec.fes.interceptor.UUIDInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author longjie
 * @since
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // UUID
        registry.addInterceptor(new UUIDInterceptor()).addPathPatterns("/**");
    }
}

```

## UUIDInterceptor.java

```
package com.kangyonggan.demo.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author kangyonggan
 * @since 5/15/18
 */
@Log4j2
public class UUIDInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 给log4j2设置线程变量uuid
        ThreadContext.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除log4j2的线程变量uuid
        ThreadContext.remove("uuid");
    }

}
```