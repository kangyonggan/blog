---
title: 学习SpringCloud06-Eureka-高可用
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 文档
[https://blog.csdn.net/sosfnima/article/details/53178305](https://blog.csdn.net/sosfnima/article/details/53178305)

## 说明
服务注册中心的高可用，其实就是冗余一个注册中心，当其中一个注册中心挂了的时候另外一个还可以继续使用，这就是注册中心的高可用。

## 目的
启动两个服务注册中心，并注册一个服务，此时两个注册中心都有此服务，kill掉其中一个注册中心，另外一个注册中心还能继续使用。

## 前提
这篇文章的实验环境是基于《学习SpringCloud03-Eureka》中的几个项目的。

## 改造eureka-server
### pom.xml
添加多环境的配置，当环境是1时，启动注册中心1，当环境是2时启动注册中心2。

<!-- more -->

```
<profiles>
	<profile>
		<id>2</id>
		<properties>
			<env>2</env>
		</properties>
	</profile>
	<profile>
		<id>1</id>
		<activation>
			<activeByDefault>true</activeByDefault>
		</activation>
		<properties>
			<env>1</env>
		</properties>
	</profile>
</profiles>
```

### application.yml
```
spring:
  profiles:
    active: @env@

eureka:
  client:
    # registerWithEureka: false 和 fetchRegistry: false 表明自己是一个eureka server.
    registerWithEureka: false
    fetchRegistry: false
```

### application-1.yml
```
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    serviceUrl:
      defaultZone: http://localhost2:8771/eureka/
```

### application-2.yml
```
server:
  port: 8771

eureka:
  instance:
    hostname: localhost2
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

## 测试
### 启动注册中心1
```
mvn clean intsall
mvn spring-boot:run
```

此时可访问：localhost:8761

### 启动注册中心2
```
mvn clean intsall -P2
mvn spring-boot:run
```

此时可访问：localhost2:8771

> 需要配置一下host文件，127.0.0.1 localhost2

### 启动一个服务提供者
服务提供者的serviceUrl指向1和2都行，观察localhost:8761和localhost2:8771，会发现都有服务。