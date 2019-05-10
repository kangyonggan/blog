---
title: 使用Spring配置多数据源
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 两个dataSources
```
    <!-- 配置dbcp数据源 - dev -->
    <bean id="dataSourceDev" class="com.alibaba.druid.pool.DruidDataSource"
          destroy-method="close">
        <property name="driverClassName" value="${ehelper.jdbc.driver}"></property>
        <property name="url" value="${ehelper.jdbc.url.dev}"></property>
        <property name="username" value="${ehelper.jdbc.username.dev}"></property>
        <property name="password" value="${ehelper.jdbc.password.dev}"></property>

        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="5"></property>
        <property name="minIdle" value="5"></property>
        <property name="maxActive" value="100"></property>

        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000" ></property>

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000" ></property>

        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="300000" ></property>

        <property name="validationQuery" value="SELECT 'x'" ></property>
        <property name="testWhileIdle" value="true" ></property>
        <property name="testOnBorrow" value="false" ></property>
        <property name="testOnReturn" value="false" ></property>

        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="true" ></property>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20" ></property>

        <!-- 关闭长时间不使用的连接 -->
        <property name="removeAbandoned" value="true" ></property> <!-- 打开removeAbandoned功能 -->
        <property name="removeAbandonedTimeout" value="1200" ></property> <!-- 1200秒，也就是20分钟 -->
        <property name="logAbandoned" value="true" ></property> <!-- 关闭abanded连接时输出错误日志 -->
    </bean>

    <!-- 配置dbcp数据源 - uat -->
    <bean id="dataSourceUat" class="com.alibaba.druid.pool.DruidDataSource"
          destroy-method="close">
        <property name="driverClassName" value="${ehelper.jdbc.driver}"></property>
        <property name="url" value="${ehelper.jdbc.url.uat}"></property>
        <property name="username" value="${ehelper.jdbc.username.uat}"></property>
        <property name="password" value="${ehelper.jdbc.password.uat}"></property>

        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="5"></property>
        <property name="minIdle" value="5"></property>
        <property name="maxActive" value="100"></property>

        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000" ></property>

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000" ></property>

        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="300000" ></property>

        <property name="validationQuery" value="SELECT 'x'" ></property>
        <property name="testWhileIdle" value="true" ></property>
        <property name="testOnBorrow" value="false" ></property>
        <property name="testOnReturn" value="false" ></property>

        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="true" ></property>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20" ></property>

        <!-- 关闭长时间不使用的连接 -->
        <property name="removeAbandoned" value="true" ></property> <!-- 打开removeAbandoned功能 -->
        <property name="removeAbandonedTimeout" value="1200" ></property> <!-- 1200秒，也就是20分钟 -->
        <property name="logAbandoned" value="true" ></property> <!-- 关闭abanded连接时输出错误日志 -->
    </bean>
```

<!-- more -->

## MultiDataSource
需要自己实现数据源路由， 继承AbstractRoutingDataSource，覆写determineCurrentLookupKey方法即可。

```
package com.shhxzq.fin.ehelper.biz.util;

import com.shhxzq.fin.ehelper.model.constants.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源
 *
 * @author kangyonggan
 * @since 4/28/17
 */
public class MultiDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal();

    public static void setDataSource(DataSource dataSource) {
        dataSourceKey.set(dataSource.name());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}
```

其中DataSource是个枚举：

```
package com.shhxzq.fin.ehelper.model.constants;

/**
 * @author kangyonggan
 * @since 4/28/17
 */
public enum DataSource {
    DEV, UAT;

    public static DataSource getDataSource(String name) {
        for (DataSource dataSource : DataSource.values()) {
            if (dataSource.name().equalsIgnoreCase(name)) {
                return dataSource;
            }
        }

        return DataSource.DEV;
    }
}

```

## sqlSessionFactory
```
<!-- 创建SqlSessionFactory，同时指定数据源 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="multiDataSource"></property>
    <property name="configLocation" value="classpath:mybatis.xml"></property>
    <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
    <property name="typeAliasesPackage" value="com.shhxzq.fin.ehelper.model.vo"></property>
    <property name="plugins">
        <array>
            <bean class="com.github.pagehelper.PageHelper">
                <property name="properties">
                    <value>
                        dialect=mysql
                    </value>
                </property>
            </bean>
        </array>
    </property>
</bean>
```

## sqlSession
```
<!-- 配置SQLSession模板 -->
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate" scope="prototype">
    <constructor-arg index="0" ref="sqlSessionFactory"></constructor>
</bean>
```

## transactionManager
```
<!-- 使用JDBC事务 -->
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="multiDataSource"></property>
</bean>
```

## transactionAdvice
```
<!-- AOP配置事物 -->
<tx:advice id="transactionAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="delete*" propagation="REQUIRED"></tx:method>
        <tx:method name="update*" propagation="REQUIRED"></tx:method>
        <tx:method name="save*" propagation="REQUIRED"></tx:method>
        <tx:method name="remove*" propagation="REQUIRED"></tx:method>
        <tx:method name="send*" propagation="REQUIRED"></tx:method>

        <tx:method name="search*" read-only="true" propagation="REQUIRED"></tx:method>
        <tx:method name="find*" read-only="true" propagation="REQUIRED"></tx:method>
        <tx:method name="get*" read-only="true" propagation="REQUIRED"></tx:method>
        <tx:method name="*" read-only="true" propagation="REQUIRED"></tx:method>
    </tx:attributes>
</tx:advice>
```

## transactionPointcut
```
<!-- 配置AOP切面 -->
<aop:config>
    <!--切点，用于事务-->
    <aop:pointcut id="transactionPointcut"
                  expression="execution(* com.shhxzq.fin.ehelper.biz.service.impl.transaction..*.*(..))"></aop:pointcut>

    <!--事物切面-->
    <aop:advisor pointcut-ref="transactionPointcut" advice-ref="transactionAdvice"></aop:advisor>
</aop:config>
```

## 方案一
原本我是想在spring初始化bean的时候就指定数据源，这样的话事务就不会和数据源打架，我想到的方案是，给不同的包指定不同的数据源，但是这样会带来一个问题，就是会冗余代码。

比如dev环境和uat环境的service分别放在dev和uat包下，再分别给dev包河uat包指定dev的数据源和uat的数据源。

引发的问题：dev和uat业务逻辑一样，仅数据源不一样，但是却有两份代码！冗余还是小事，以后维护才是大事，所以此方案果断排除。

## 方案二
在调用方法的时候传入一个参数，指定调用哪个数据源，这样代码就没冗余的地方了，但是会带来一个问题。

就是需要修改老代码，在参数中增加一个参数，然后在方法中指定数据源，在方法中指定数据源可以使用注解和切面完成，但不可避面的还是要增加一个参数。

如果不增加一个参数，而是在注解中加参数呢？我也这么想过，但是这样的话，这个方法就只能使用固定的数据源了，达不到动态的效果。

进一步分析，如果在注解中加参数，另外再多写一个方法指定为另一个数据源呢？显然是不可取的，方法冗余，维护困难，如果再次增加数据源还得再加一个方法。

所以，最后我还是选择了使用【传参+注解】的方案，没办法，要想动态切换数据源，你总的告诉方法你要用哪个数据源吧，怎么告诉他？传参是最好途径了，并且扩展性强。下面是我具体实现代码。

## DataSourceSwitch
```
package com.shhxzq.fin.ehelper.model.annotation;

import java.lang.annotation.*;

/**
 * 在方法上加此注解，会使用第一个参数即DataSource枚举切换数据源
 *
 * @author kangyonggan
 * @since 2016/12/8
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceSwitch {

}
```

## DataSourceAop
```
package com.shhxzq.fin.ehelper.biz.aop;

import com.shhxzq.fin.ehelper.biz.util.MultiDataSource;
import com.shhxzq.fin.ehelper.model.annotation.DataSourceSwitch;
import com.shhxzq.fin.ehelper.model.constants.DataSource;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author kangyonggan
 * @since 4/28/17
 */
@Log4j2
@Component
@Aspect
public class DataSourceAop {

    @Pointcut("execution(* com.shhxzq.fin.ehelper.biz.service.impl..*.*(..))")
    public void pointcut() {
    }

    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object args[] = joinPoint.getArgs();
        Class clazz = joinPoint.getTarget().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = clazz.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        DataSourceSwitch dataSourceSwitch = method.getAnnotation(DataSourceSwitch.class);
        if (dataSourceSwitch != null) {
            log.info("使用指定的数据源.");
            if (args.length > 0) {
                Object obj = args[0];
                if (obj instanceof DataSource) {
                    DataSource dataSource = (DataSource) obj;
                    MultiDataSource.setDataSource(dataSource);
                    log.info("数据源：{}", dataSource.name());
                } else {
                    log.info("第一个参数不是DataSource枚举，所以仍然使用默认数据源.");
                }
            } else {
                log.info("第一个参数不是DataSource枚举，所以仍然使用默认数据源.");
            }
        } else {
            log.info("使用默认数据源.");
        }


        return joinPoint.proceed(args);
    }
}
```

## BeCommandServiceImpl
```
package com.shhxzq.fin.ehelper.biz.service.impl;

import com.shhxzq.fin.ehelper.biz.service.BeCommandService;
import com.shhxzq.fin.ehelper.model.annotation.DataSourceSwitch;
import com.shhxzq.fin.ehelper.model.annotation.LogTime;
import com.shhxzq.fin.ehelper.model.constants.DataSource;
import com.shhxzq.fin.ehelper.model.vo.BeCommand;
import org.springframework.stereotype.Service;

/**
 * @author kangyonggan
 * @since 4/28/17
 */
@Service
public class BeCommandServiceImpl extends BaseService<BeCommand> implements BeCommandService {

    @Override
    @LogTime
    @DataSourceSwitch
    public BeCommand findBeCommandBySerialNo(DataSource dataSource, String serialNo) {
        BeCommand beCommand = new BeCommand();
        beCommand.setSerialNo(serialNo);

        return super.selectOne(beCommand);
    }
}
```

## 问题
另外我还遇到了一个问题，那就是事务和多数据源打架了，我的解决方案是把它们两个分开。但也不是太好，先这样吧，以后再研究。



