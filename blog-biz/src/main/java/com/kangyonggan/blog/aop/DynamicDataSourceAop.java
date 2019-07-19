package com.kangyonggan.blog.aop;

import com.kangyonggan.blog.annotation.DataSourceSwitch;
import com.kangyonggan.blog.configuration.DynamicDataSource;
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
public class DynamicDataSourceAop {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.kangyonggan.blog.service.*.*(..))")
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
        Class clazz = joinPoint.getTarget().getClass();

        DataSourceSwitch dataSourceSwitch = (DataSourceSwitch) clazz.getDeclaredAnnotation(DataSourceSwitch.class);
        if (dataSourceSwitch != null) {
            DynamicDataSource.setDataSource(dataSourceSwitch.value());
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        dataSourceSwitch = method.getAnnotation(DataSourceSwitch.class);
        if (dataSourceSwitch != null) {
            DynamicDataSource.setDataSource(dataSourceSwitch.value());
        }

        Object result;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            DynamicDataSource.remove();
        }

        return result;
    }

}
