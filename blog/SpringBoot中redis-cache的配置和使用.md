---
title: SpringBoot中redis-cache的配置和使用
date: 2019-03-27 12:33:55
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

<!--redis-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

<!-- more -->

## 配置
`application.yml`:

```
spring:
  cache:
    redis:
      # 缓存有效期30分钟
      time-to-live: 1800000
  redis:
    host: localhost
    port: 6379
    password: 123456
```

`RedisConfigurer.java`:

```
package com.kangyonggan.demo.configuration;

import org.springframework.cache.annotation.EnableCaching;
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
@EnableCaching
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

## 使用
```
package com.kangyonggan.demo.service.impl;

import com.kangyonggan.demo.annotation.CacheDel;
import com.kangyonggan.demo.model.User;
import com.kangyonggan.demo.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kangyonggan
 * @since 12/6/18
 */
@Service
@CacheConfig(cacheNames = "demo:user")
public class UserServiceImpl extends BaseService<User> implements UserService {

    /**
     * 如果有缓存，则直接返回缓存，否则执行方法体，并缓存方法结果，其中key="demo:user::email:admin@kangyonggan.com"
     *
     * @param email
     * @return
     */
    @Override
    @Cacheable(key = "'email:' + #email")
    public User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return myMapper.selectOne(user);
    }

    /**
     * 如果有缓存，则直接返回缓存，否则执行方法体，并缓存方法结果，其中key="demo:user::all"
     *
     * @return
     */
    @Override
    @Cacheable(key = "'all'")
    public List<User> findAllUsers() {
        return myMapper.selectAll();
    }

    /**
     * 清除缓存。key="demo:user::all"
     *
     * @param user
     */
    @Override
    @CacheEvict(key = "'all'")
    public void saveUser(User user) {
        myMapper.insertSelective(user);
    }

    /**
     * 清除缓存。key="demo:user::all"
     * 清除缓存。key="demo:user::email:*"
     *
     * @param user
     */
    @Override
    @CacheEvict(key = "'all'")
    @CacheDel("demo:user::email:*")
    public void updateUser(User user) {
        myMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 清除缓存。key="demo:user::all"
     * 清除缓存。key="demo:user::email:*"
     *
     * @param userId
     */
    @Override
    @CacheEvict(key = "'all'")
    @CacheDel("demo:user::email:*")
    public void deleteUser(Long userId) {
        myMapper.deleteByPrimaryKey(userId);
    }
}
```

## 缓存注解AOP
由于@CacheEvict并不支持模糊匹配，所以自定义了一个@CacheDel注解。

`CacheDel.java`:

```
package com.kangyonggan.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 清除缓存，支持模糊匹配（*）
 *
 * @author kangyonggan
 * @since 2018/6/3 0003
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDel {

    /**
     * 缓存的key。
     *
     * @return
     */
    String[] value();

}

```

`RedisCacheAop.java`:

```
package com.kangyonggan.demo.aop;

import com.kangyonggan.demo.annotation.CacheDel;
import com.kangyonggan.demo.service.impl.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author kangyonggan
 * @since 2019-03-26
 */
@Aspect
@Configuration
public class RedisCacheAop {

    @Autowired
    private RedisService redisService;

    /**
     * 切入点
     */
    @Pointcut("execution(* com.kangyonggan.demo.service.*.*(..))")
    public void pointCut() {
    }

    /**
     * 方法执行之前
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        Class clazz = joinPoint.getTarget().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        CacheDel cacheDel = method.getAnnotation(CacheDel.class);
        if (cacheDel != null) {
            String[] keys = cacheDel.value();
            for (String key : keys) {
                redisService.deleteAll(key);
            }
        }
    }

}
```

`RedisService.java`:

```
package com.kangyonggan.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author kangyonggan
 * @since 8/14/18
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param value
     * @return
     */
    public boolean set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        return true;
    }

    /**
     * set
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
        return true;
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * get
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Object get(String key, Object defaultValue) {
        Object val = redisTemplate.opsForValue().get(key);

        return val == null ? defaultValue : val;
    }

    /**
     * get and update expire
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object getAndUpdateExpire(String key, long timeout, TimeUnit unit) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            redisTemplate.expire(key, timeout, unit);
        }
        return object;
    }

    /**
     * multiGet
     *
     * @param keys
     * @return
     */
    public List<Object> multiGet(Set<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * delete
     *
     * @param key
     * @return
     */
    public Object delete(String key) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            redisTemplate.delete(key);
        }
        return object;
    }

    /**
     * delete all like pattern
     *
     * @param pattern
     * @return
     */
    public void deleteAll(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    /**
     * incr
     *
     * @param key
     * @return
     */
    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key
     * @param values
     * @return
     */
    public long leftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * @param key
     * @return
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * listRange
     *
     * @param key
     * @return
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * hashSetNx
     *
     * @param hash
     * @param key
     * @param value
     * @return
     */
    public boolean hashSetNx(String hash, String key, String value) {
        return redisTemplate.opsForHash().putIfAbsent(hash, key, value);
    }

    /**
     * hashSize
     *
     * @param hash
     * @return
     */
    public long hashSize(String hash) {
        return redisTemplate.opsForHash().size(hash);
    }

    /**
     * hashExist
     *
     * @param hash
     * @param key
     * @return
     */
    public boolean hashExist(String hash, String key) {
        return redisTemplate.opsForHash().hasKey(hash, key);
    }

    /**
     * get keys of pattern
     *
     * @param pattern
     * @return
     */
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

}

```