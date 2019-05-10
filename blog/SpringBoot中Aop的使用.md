---
title: SpringBoot中Aop的使用
date: 2019-03-26 15:32:21
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

<!--spring-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>
```

<!-- more -->

## 打印方法出入参
`MethodLogAop.java`:

```
package com.kangyonggan.demo.aop;

import com.alibaba.fastjson.JSON;
import com.kangyonggan.demo.annotation.MethodLog;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author kangyonggan
 * @since 2019-03-26
 */
@Aspect
@Configuration
@Log4j2
public class MethodLogAop {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.kangyonggan.demo.service.*.*(..))")
    public void pointCut() {
    }

    /**
     * 环绕方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Class clazz = joinPoint.getTarget().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        String targetName = "[" + clazz.getName() + "." + method.getName() + "]";

        MethodLog methodLog = method.getAnnotation(MethodLog.class);
        Object result;
        if (methodLog != null) {
            log.info("进入方法:{} - args: {}", targetName, JSON.toJSONString(args));

            long beginTime = System.currentTimeMillis();
            result = joinPoint.proceed(args);
            long endTime = System.currentTimeMillis();
            long time = endTime - beginTime;

            log.info("离开方法:{} - return: {}", targetName, JSON.toJSONString(result));
            log.info("方法耗时:{}ms - {}", time, targetName);
        } else {
            result = joinPoint.proceed(args);
        }

        return result;
    }
}
```

其中`MethodLog.java`: 

```
package com.kangyonggan.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法日志。打印出参、入参和执行时间
 *
 * @author kangyonggan
 * @since 2018/6/3 0003
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

}
```