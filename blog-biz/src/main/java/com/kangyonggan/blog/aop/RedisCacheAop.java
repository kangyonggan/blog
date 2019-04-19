package com.kangyonggan.blog.aop;

import com.kangyonggan.blog.annotation.CacheDel;
import com.kangyonggan.blog.service.impl.RedisService;
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
    @Pointcut("execution(* com.kangyonggan.blog.service.*.*(..))")
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
