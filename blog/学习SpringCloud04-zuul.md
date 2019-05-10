---
title: 学习SpringCloud04-zuul
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

 ## 文档
[https://blog.csdn.net/forezp/article/details/70148833/](https://blog.csdn.net/forezp/article/details/70148833/)

## 目的
Zuul的主要功能是路由转发和过滤器，本文对此进行实验，请求/a/hi?name=hahaha转发到a服务，请求/b/hi?name=hahaha转发到b服务。

zuul主要有以下功能：
- 认证
- 洞察
- 压力测试
- 金丝雀测试
- 动态路由
- 服务迁移
- 负载脱落
- 安全
- 静态响应处理
- 主动/主动流量管理

<!-- more -->

## 前提
这篇文章的实验环境是基于《学习SpringCloud03-Eureka》中的几个项目的，先启动注册中`eureka-server`, 再启动服务提供者1`eureka-provider1`。
将服务提供者2`eureka-provider2`的Application Name改为`service-hello`并启动。

## 路由网关
创建一个新项目`eureka-zuul`。

### pom.xml
```

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.3.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### application.yml
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
server:
  port: 8765
spring:
  application:
    name: service-zuul
zuul:
  routes:
    a:
      path: /a/**
      serviceId: service-hi
    b:
      path: /b/**
      serviceId: service-hello
```

### Application.java
```
package com.kangyonggan.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

启动后分别访问
-  [http://localhost:8765/a/hi?name=abc](http://localhost:8765/a/hi?name=abc)
响应：`hi abc,i am from port:8762`
-  [http://localhost:8765/b/hi?name=abc](http://localhost:8765/b/hi?name=abc)
响应：`hi abc,i am from port:8763`

zuul还有很多强大的功能，学习入门阶段浅尝辄止即可，待后面搭建项目时再系统的学习。