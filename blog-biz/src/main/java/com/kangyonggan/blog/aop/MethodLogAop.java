package com.kangyonggan.blog.aop;

import com.alibaba.fastjson.JSON;
import com.kangyonggan.blog.annotation.MethodLog;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
     * 环绕方法
     *
     * @param joinPoint
     * @param methodLog
     * @return
     * @throws Throwable
     */
    @Around("@annotation(methodLog)")
    public Object around(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Class clazz = joinPoint.getTarget().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        String targetName = "[" + clazz.getName() + "." + method.getName() + "]";

        log.info("进入方法:{} - args: {}", targetName, JSON.toJSONString(args));
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(args);
        long endTime = System.currentTimeMillis();
        long time = endTime - beginTime;

        log.info("离开方法:{} - return: {}", targetName, JSON.toJSONString(result));
        log.info("方法耗时:{}ms - {}", time, targetName);

        return result;
    }


}
