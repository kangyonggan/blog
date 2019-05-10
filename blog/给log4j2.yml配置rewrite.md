---
title: 给log4j2.yml配置rewrite
date: 2019-04-15 10:10:06
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
    Rewrite:
      name: MyRewrite
      ignoreExceptions: true
      MyRewritePolicy:
        debug: false
      AppenderRef:
        - ref: STDOUT

    Console:
      name: STDOUT
      PatternLayout:
        pattern: "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"

  Loggers:
    Root:
      level: info
      additivity: true
      AppenderRef:
        - ref: MyRewrite

```

## MyRewritePolicy.java

```
package com.kangyonggan.demo.util;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * @author kangyonggan
 * @since 2019/4/14 0014
 */
@Plugin(name = "MyRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public class MyRewritePolicy implements RewritePolicy {

    private boolean debug;

    public MyRewritePolicy(boolean debug) {
        this.debug = debug;
    }

    @Override
    public LogEvent rewrite(LogEvent event) {
        if (debug) {
            return event;
        }

        Message msg = event.getMessage();
        if (msg == null) {
            return event;
        }
        String message = msg.getFormattedMessage();

        // 处理日志的逻辑
        if (null != message) {
            message = processData(message);
        }
        SimpleMessage simpleMessage = new SimpleMessage(message);
        return new Log4jLogEvent.Builder(event).setMessage(simpleMessage).build();
    }

    /**
     * 敏感信息处理：手机号、身份证号处理。只需传入要处理的字符串，自动处理满足手机号或身份证格式的数字
     *
     * @param str
     * @return
     */
    private String processData(String str) {
        // TODO 自行实现
        return str;
    }

    @PluginFactory
    public static MyRewritePolicy factory(@PluginAttribute("debug") String debug) {
        return new MyRewritePolicy(Booleans.parseBoolean(debug, true));
    }

}

```