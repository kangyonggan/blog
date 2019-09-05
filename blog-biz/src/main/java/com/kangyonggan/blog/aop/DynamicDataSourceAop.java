package com.kangyonggan.blog.aop;

import com.kangyonggan.blog.annotation.DataSourceSwitch;
import com.kangyonggan.blog.configuration.DynamicDataSource;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * @author kangyonggan
 * @since 2019-03-26
 */
@Aspect
@Configuration
@Log4j2
public class DynamicDataSourceAop {

    /**
     * 环绕方法
     *
     * @param joinPoint
     * @param dataSourceSwitch
     * @return
     * @throws Throwable
     */
    @Around("@annotation(dataSourceSwitch)")
    public Object around(ProceedingJoinPoint joinPoint, DataSourceSwitch dataSourceSwitch) throws Throwable {
        DynamicDataSource.setDataSource(dataSourceSwitch.value());

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
