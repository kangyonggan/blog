---
title: Redis简记
date: 2019-03-26 18:56:54
categories: 系统运维
tags:
- Linux
---

# Redis简记

## 安装

```
# 下载
wget http://download.redis.io/releases/redis-5.0.3.tar.gz
# 解压
tar -zxvf redis-5.0.3.tar.gz -C ../install/
# 编译
cd src/
sudo make & sudo make install
```

<!-- more -->

如果没安装make

```
sudo apt-get install make
```
如果没安装gcc

```
sudo apt-get install gcc
```
如果报错：jemalloc/jemalloc.h: No such file or directory

```
make MALLOC=libc
```

## 启动

```
redis-server redis.conf &
```
## 停止

```
redis-cli -a 123456 shutdown
```

## 集群（小哨兵）
### master
配置主库

```
# vi redis.conf
# bind 127.0.0.1
requirepass "123456"
masterauth "123456"
```

启动主库

```
redis-server redis.conf &
# 登录
redis-cli -a 123456
# 查看信息
INFO
```
### sentinel
配置哨兵

```
# vi sentinel.conf
sentinel monitor mymaster 192.168.0.100 6379 2
sentinel auth-pass mymaster 123456
```
参数2表示：只要sentinel集群中有2个认为master无法连接之后，就会进行主从切换。

启动哨兵

```
redis-sentinel sentinel.conf &
```

### slave
配置从库

```
# vi redis.conf
# bind 127.0.0.1
requirepass "123456"
replicaof 192.168.0.100 6379
masterauth "123456"
```
启动从库
```
redis-server redis.conf &
# 登录
redis-cli -a 123456
# 查看信息
INFO
```
### sentinel
配置哨兵

```
# vi sentinel.conf
sentinel monitor mymaster 192.168.0.100 6379 2
sentinel auth-pass mymaster 123456
```
启动哨兵

```
redis-sentinel sentinel.conf &
```

## 测试
如果想弄清楚小哨兵的工作模式，必须亲手测试，比如杀死主库等。

## SpringBoot
### 配置
1.application.yml
```
spring:
  redis:
    host: localhost
    port: 6379
    password: 123456
    sentinel:
      master: mymaster
      nodes: localhost:26379
```

2.RedisConfigurer.java

```
package com.kangyonggan.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author kangyonggan
 * @since 2019-03-26
 */
@Configuration
public class RedisConfigurer {

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        return redisTemplate;
    }
}
```

