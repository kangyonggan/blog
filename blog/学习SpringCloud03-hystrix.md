---
title: 学习SpringCloud03-hystrix
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---



## 文档
[https://blog.csdn.net/forezp/article/details/70148833/](https://blog.csdn.net/forezp/article/details/70148833/)

## 目的
尝试使用熔断器。即服务消费者去调用服务提供者的时候，如果由于各种原因提供者不可用了，并且达到一个阀值（Hystric 是5秒20次）断路器将会被打开。当提供者再次可用时，调用会恢复正常。

## 前提
这篇文章的实验环境是基于《学习SpringCloud03-Eureka》中的几个项目的，先启动注册中`eureka-server`, 再启动服务提供者1`eureka-provider1`，服务提供者2就不用启动了，这篇文章不是为了实验集群和负载均衡。

## 改造消费者
### pom.xml
在`eureka-consumer`的pom中加入依赖：  
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

<!-- more -->

### Application.java
添加注解`@EnableHystrix`:  
```
package com.kangyonggan.sc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

### HelloService.java
改造后：  
```
package com.kangyonggan.sc;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author kangyonggan
 * @since 6/20/18
 */
@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="   name, String.class);
    }

    public String hiError(String name) {
        return "hi,"   name   ",sorry,error!";
    }
}
```

启动后访问http://localhost:8764/hi?name=小新。会有正常的响应，然后把服务提供者（eureka-provider1）关闭，再次请求，响应如下：

```
hi,forezp,sorry,error!
```

再次启动服务提供者（eureka-provider1）并发起请求，又可以正常响应。

> 注1. 本次没能实验出熔断阈值（5秒20次）
> 注2. 貌似还有其他流弊的用法，本次入门暂不多研究，后面再细看。
