---
title: SpringBoot中log4j2.yml的配置和使用
date: 2019-03-27 10:19:06
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

<!--log4j2 yml-->
<jackson-dataformat-yaml.version>2.9.8</jackson-dataformat-yaml.version>

...

<!--log4j2 yml-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>${jackson-dataformat-yaml.version}</version>
</dependency>
```

<!-- more -->

## log4j2.yml
```
Configuration:
  status: warn
  monitorInterval: 300

  properties:
    property:
      - name: pattern
        value: "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"
      - name: path
        value: /Users/kyg/logs/demo

  Appenders:
    RollingFile:
      - name: AllFile
        fileName: ${path}/all.log
        filePattern: "${path}/archive/$${date:yyyy-MM}/all-%d{yyyy-MM-dd}-%i.log.gz"
        PatternLayout:
          pattern: ${pattern}
        Filters:
          ThresholdFilter:
            - level: fatal
              onMatch: DENY
              onMismatch: NEUTRAL
            - level: debug
              onMatch: ACCEPT
              onMismatch: DENY
        Policies:
          TimeBasedTriggeringPolicy:
            interval: 1
          SizeBasedTriggeringPolicy:
            size: "500 MB"
        DefaultRolloverStrategy:
          max: 200

      - name: ErrorFile
        fileName: ${path}/error.log
        filePattern: "${path}/archive/$${date:yyyy-MM}/error-%d{yyyy-MM-dd}-%i.log.gz"
        PatternLayout:
          pattern: ${pattern}
        Filters:
          ThresholdFilter:
            - level: fatal
              onMatch: DENY
              onMismatch: NEUTRAL
            - level: error
              onMatch: ACCEPT
              onMismatch: DENY
        Policies:
          TimeBasedTriggeringPolicy:
            interval: 1
          SizeBasedTriggeringPolicy:
            size: "500 MB"
          DefaultRolloverStrategy:
            max: 200

  Loggers:
    Logger:
      name: com.kangyonggan.demo.mapper
      level: debug
      additivity: false
      AppenderRef:
        - ref: AllFile
        - ref: ErrorFile

    Root:
      level: info
      additivity: true
      AppenderRef:
        - ref: AllFile
        - ref: ErrorFile
```