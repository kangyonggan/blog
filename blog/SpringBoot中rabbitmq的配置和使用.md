---
title: SpringBoot中rabbitmq的配置和使用
date: 2019-03-28 14:41:02
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

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

<!-- more -->

## 配置
`application.yml`:

```
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

`RabbitConfigurer.java`:

```
package com.kangyonggan.demo.configuration;

import com.kangyonggan.demo.constants.RabbitQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kangyonggan
 * @since 2019-03-28
 */
@Configuration
public class RabbitConfigurer {

    @Bean
    public Queue queueDemo() {
        return new Queue(RabbitQueue.QUEUE_DEMO);
    }

}

```

## 使用（生产者）
`RabbitMQTest.java`: 

```
package com.kangyonggan.demo;

import com.kangyonggan.demo.constants.RabbitQueue;
import com.kangyonggan.demo.model.User;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author kangyonggan
 * @since 2019-03-28
 */
public class RabbitMQTest extends AbstractTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void convertAndSend() throws Exception {
        User user = new User();
        user.setEmail("admin@kangyonggan.com");
        user.setCreatedTime(new Date());
        rabbitTemplate.convertAndSend(RabbitQueue.QUEUE_DEMO, user);

        System.in.read();
    }

}

```

## 消费者
`RabbitConsumer.java`: 

```
package com.kangyonggan.demo.rabbit;

import com.kangyonggan.demo.constants.RabbitQueue;
import com.kangyonggan.demo.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author kangyonggan
 * @since 2019-03-28
 */
@Component
@Log4j2
public class RabbitConsumer {

    @RabbitListener(queues = RabbitQueue.QUEUE_DEMO)
    public void process(Message message) throws Exception {
        User user = convert(message.getBody());

        log.info("mq receive:{}", user);
    }

    private <T> T convert(byte[] body) throws Exception {
        ByteArrayInputStream bodyIs = new ByteArrayInputStream(body);
        ObjectInputStream in = new ObjectInputStream(bodyIs);
        return (T) in.readObject();
    }

}

```