---
title: 使用Log4j2的Rewrite过滤敏感信息
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## 摸索过程
查看官方文档[https://logging.apache.org/log4j/2.0/manual/appenders.html#RewriteAppender](https://logging.apache.org/log4j/2.0/manual/appenders.html#RewriteAppender)，官方文档中虽然有所介绍，但是没给demo，百度谷歌都很难查到此类文章，所有配置起来有一定难度。

<!-- more -->

### pom.xml的配置
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.kangyonggan.demo</groupId>
    <artifactId>log4j2-rewrite</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <log4j2.api.version>2.8.2</log4j2.api.version>
    </properties>

    <dependencies>
        <!--Log4j2-->
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>${log4j2.api.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>${log4j2.api.version}</version>
        </dependency>
    </dependencies>

</project>
```

### log4j2.xml的配置
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="warn">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"></PatternLayout>
        </Console>

        <Rewrite name="Rewrite">
            <MyRewritePolicy></MyRewritePolicy>
            <AppenderRef ref="Console"></AppenderRef>
        </Rewrite>
    </Appenders>

    <Loggers>
        <Root level="info">
            <AppenderRef ref="Rewrite"></AppenderRef>
        </Root>
    </Loggers>
</Configuration>
```

其中MyRewritePolicy是我自定义的，它实现了RewritePolicy接口，同时需要实现一个方法public LogEvent rewrite(final LogEvent event)

### MyRewritePolicy.java的实现
```
package com.kangyonggan.demo;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * @author kangyonggan
 * @since 2017/4/24 0024
 */
public final class MyRewritePolicy implements RewritePolicy {

    @Override
    public LogEvent rewrite(final LogEvent event) {
        String message = event.getMessage().getFormattedMessage();
        // 处理日志的逻辑
        message = "处理后的日志: " + message;

        SimpleMessage simpleMessage = new SimpleMessage(message);
        LogEvent result = new Log4jLogEvent(event.getLoggerName(), event.getMarker(),
                event.getLoggerFqcn(), event.getLevel(), simpleMessage,
                event.getThrown(), event.getContextMap(), event.getContextStack(),
                event.getThreadName(), event.getSource(), event.getTimeMillis());

        return result;
    }
}

```

### 小测一下
```
package com.kangyonggan.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author kangyonggan
 * @since 2017/4/24 0024
 */
public class Test {

    private static Logger logger = LogManager.getLogger(Test.class);

    public static void main(String[] args) {
        logger.info("手机号:{}", "15121149571");
    }

}

```

运行后报错：

```
2017-04-24 23:35:04,827 main ERROR Unable to invoke factory method in class class com.kangyonggan.demo.MyRewritePolicy for element MyRewritePolicy. java.lang.IllegalStateException: No factory method found for class com.kangyonggan.demo.MyRewritePolicy
	at org.apache.logging.log4j.core.config.plugins.util.PluginBuilder.findFactoryMethod(PluginBuilder.java:224)
	at org.apache.logging.log4j.core.config.plugins.util.PluginBuilder.build(PluginBuilder.java:130)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.createPluginObject(AbstractConfiguration.java:952)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.createConfiguration(AbstractConfiguration.java:892)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.createConfiguration(AbstractConfiguration.java:884)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.createConfiguration(AbstractConfiguration.java:884)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.doConfigure(AbstractConfiguration.java:508)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.initialize(AbstractConfiguration.java:232)
	at org.apache.logging.log4j.core.config.AbstractConfiguration.start(AbstractConfiguration.java:244)
	at org.apache.logging.log4j.core.LoggerContext.setConfiguration(LoggerContext.java:545)
	at org.apache.logging.log4j.core.LoggerContext.reconfigure(LoggerContext.java:617)
	at org.apache.logging.log4j.core.LoggerContext.reconfigure(LoggerContext.java:634)
	at org.apache.logging.log4j.core.LoggerContext.start(LoggerContext.java:229)
	at org.apache.logging.log4j.core.impl.Log4jContextFactory.getContext(Log4jContextFactory.java:152)
	at org.apache.logging.log4j.core.impl.Log4jContextFactory.getContext(Log4jContextFactory.java:45)
	at org.apache.logging.log4j.LogManager.getContext(LogManager.java:194)
	at org.apache.logging.log4j.LogManager.getLogger(LogManager.java:551)
	at com.kangyonggan.demo.Test.<clinit>(Test.java:12)

[INFO ] 2017-04-24 23:35:04.884 [com.kangyonggan.demo.Test.main:15] - 手机号:15121149571
```

从报错信息中可以看出：没有工厂方法（No factory method found）。
还提到了org.apache.logging.log4j.core.config.plugins这个包下面的插件。
所以去这个包下面看一下相关的插件：

![plugin](/upload/article/log4j2-rewrite-01.png)

果然看到了factory相关的plugin，并且PluginFactory是作用在method上的。

### 使用@PluginFactory注解

```
package com.kangyonggan.demo;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * @author kangyonggan
 * @since 2017/4/24 0024
 */
public final class MyRewritePolicy implements RewritePolicy {

    public LogEvent rewrite(final LogEvent event) {
        String message = event.getMessage().getFormattedMessage();
        // 处理日志的逻辑
        message = "处理后的日志: " + message;

        SimpleMessage simpleMessage = new SimpleMessage(message);
        LogEvent result = new Log4jLogEvent(event.getLoggerName(), event.getMarker(),
                event.getLoggerFqcn(), event.getLevel(), simpleMessage,
                event.getThrown(), event.getContextMap(), event.getContextStack(),
                event.getThreadName(), event.getSource(), event.getTimeMillis());

        return result;
    }

    @PluginFactory
    public static void factory() {
        System.out.println("factory");
    }
}

```

这次运行后没有报错了，也打印了“factory”，但是rewrite方法没被执行。

仔细一想，这既然是工厂方法，一定是为了创建对象的，所以再次修改如下。

### PluginFactory返回自定义的实例
```
package com.kangyonggan.demo;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;

/**
 * @author kangyonggan
 * @since 2017/4/24 0024
 */
public final class MyRewritePolicy implements RewritePolicy {

    public LogEvent rewrite(final LogEvent event) {
        String message = event.getMessage().getFormattedMessage();
        // 处理日志的逻辑
        message = "处理后的日志: " + message;

        SimpleMessage simpleMessage = new SimpleMessage(message);
        LogEvent result = new Log4jLogEvent(event.getLoggerName(), event.getMarker(),
                event.getLoggerFqcn(), event.getLevel(), simpleMessage,
                event.getThrown(), event.getContextMap(), event.getContextStack(),
                event.getThreadName(), event.getSource(), event.getTimeMillis());

        return result;
    }

    @PluginFactory
    public static MyRewritePolicy factory() {
        System.out.println("factory");
        return new MyRewritePolicy();
    }
}
```

在此运行后发现成功了！

### 完整log4j2.xml的配置
```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" monitorInterval="300">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"></PatternLayout>
        </Console>
        <RollingRandomAccessFile name="AllFile" fileName="${dfjz.log4j2.home}/all.log"
                                 filePattern="${dfjz.log4j2.home}/all-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"></PatternLayout>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1"></TimeBasedTriggeringPolicy>
                <SizeBasedTriggeringPolicy size="500 MB"></SizeBasedTriggeringPolicy>
            </Policies>
            <DefaultRolloverStrategy max="30"></DefaultRolloverStrategy>
            <Filters>
                <ThresholdFilter level="fatal" onMatch="DENY" onMismatch="NEUTRAL"></ThresholdFilter>
                <ThresholdFilter level="debug" onMatch="ACCEPT" onMismatch="DENY"></ThresholdFilter>
            </Filters>
        </RollingRandomAccessFile>
        <RollingRandomAccessFile name="ErrorFile" fileName="${dfjz.log4j2.home}/error.log"
                                 filePattern="${dfjz.log4j2.home}/error-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%logger{36}.%t:%L] - %msg%n"></PatternLayout>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1"></TimeBasedTriggeringPolicy>
                <SizeBasedTriggeringPolicy size="500 MB"></SizeBasedTriggeringPolicy>
            </Policies>
            <DefaultRolloverStrategy max="30"></DefaultRolloverStrategy>
            <Filters>
                <ThresholdFilter level="fatal" onMatch="DENY" onMismatch="NEUTRAL"></ThresholdFilter>
                <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"></ThresholdFilter>
            </Filters>
        </RollingRandomAccessFile>

        <SMTP name="Mail" subject="${dfjz.app.name} - 报警通知" to="${dfjz.mail.receiver}" from="${dfjz.mail.username}"
              smtpHost="${dfjz.mail.host}" smtpUsername="${dfjz.mail.username}" smtpPassword="${dfjz.mail.password}" bufferSize="${dfjz.mail.bufferSize}" >
        </SMTP>
        <Async name="AsyncAll">
            <AppenderRef ref="AllFile"></AppenderRef>
        </Async>
        <Async name="AsyncError">
            <AppenderRef ref="ErrorFile"></AppenderRef>
            <AppenderRef ref="Mail" ></AppenderRef>
        </Async>

        <Rewrite name="Rewrite">
            <MyRewritePolicy></MyRewritePolicy>
            <AppenderRef ref="Console"></AppenderRef>
            <AppenderRef ref="AsyncAll"></AppenderRef>
            <AppenderRef ref="AsyncError"></AppenderRef>
        </Rewrite>
    </Appenders>
    <Loggers>
        <Root level="debug" additivity="true">
            <AppenderRef ref="Rewrite"></AppenderRef>
        </Root>
    </Loggers>
</Configuration>
```

用以上配置就已经可以解决我们项目中的所有需求了，不对，还漏了一个，就是普通日志只打印info级别的，sql需要打印debug级别的。

### 打印debug级别的SQL
请参考我的另一篇文章[使用Log4j2让项目输出info级别的日志和debug级别的sql](https://kangyonggan.com/#article/41)
> 觉得不错就赏点吧，你的支持是我进步的动力！








