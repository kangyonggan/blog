---
title: 学习Activiti 6.0.0 （二）
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

> 本例代码：[https://github.com/kangyonggan/acti-01.git](https://github.com/kangyonggan/acti-01.git)

## 一、创建一个流程引擎`ProcessEngine`
### 1.1 添加Maven依赖
```
<activiti.version>6.0.0</activiti.version>
<mysql.version>5.1.34</mysql.version>

...

<!--activiti-->
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-engine</artifactId>
    <version>${activiti.version}</version>
</dependency>

<!--mysql-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```

<!-- more -->

### 1.2 创建流程引擎
```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo01 {

    public static void main(String[] args) {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration
            .createStandaloneProcessEngineConfiguration()
            .setJdbcDriver("com.mysql.jdbc.Driver")
            .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/acti?characterEncoding=UTF-8")
            .setJdbcUsername("root")
            .setJdbcPassword("123456")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        System.out.println(processEngine.getName());
    }

}
```

或者把jdbc的连接信息写入配置文件，如下：

### 1.3 添加配置`activiti.cfg.xml`
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/acti?characterEncoding=UTF-8"></property>
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUsername" value="root"></property>
        <property name="jdbcPassword" value="123456"></property>

        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>

</beans>
```

### 1.4 使用配置文件创建流程引擎
```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo01 {

    public static void main(String[] args) {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = cfg.buildProcessEngine();
        System.out.println(processEngine.getName());
    }

}
```

运行后输出：`default`

## 二、集成到Spring
### 2.1 添加Maven依赖
```
<spring.version>4.2.5.RELEASE</spring.version>
<junit.version>4.11</junit.version>

...

<!--activiti-->
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring</artifactId>
    <version>${activiti.version}</version>
</dependency>

<!--spring-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
    <scope>test</scope>
</dependency>
        
<!--junit-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
    <version>${junit.version}</version>
</dependency>
```

### 2.2 添加配置`activiti.cfg.xml`
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="jdbcUrl" value="jdbc:mysql://127.0.0.1:3306/acti?characterEncoding=UTF-8"></property>
        <property name="jdbcDriver" value="com.mysql.jdbc.Driver"></property>
        <property name="jdbcUsername" value="root"></property>
        <property name="jdbcPassword" value="123456"></property>

        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
        <property name="processEngineConfiguration" ref="processEngineConfiguration"></property>
    </bean>

</beans>
```

### 2.3 Junit测试代码
```
package com.kangyonggan.acti;

import org.activiti.engine.ProcessEngine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kangyonggan
 * @date 4/11/18
 */
public class Demo03 extends AbstractServiceTest {

    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void test01() {
        System.out.println(processEngine.getName());
    }

}
```

其中`AbstractServiceTest.java`代码如下：

```
package com.kangyonggan.acti;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 没有事务
 *
 * @author kangyonggan
 * @date 2016/11/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/activiti.cfg.xml"})
public abstract class AbstractServiceTest extends AbstractJUnit4SpringContextTests {

}
```

运行后输出：`default`

## 三、使用`DataSource`
### 3.1 添加Maven依赖
```
<druid.version>1.0.18</druid.version>

...

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>${druid.version}</version>
</dependency>
```

### 3.2 添加配置`activiti.cfg.xml`
```
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 配置dbcp数据源 - dev -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
          destroy-method="close">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql://127.0.0.1:3306/acti?characterEncoding=UTF-8"></property>
        <property name="username" value="root"></property>
        <property name="password" value="123456"></property>

        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="5"></property>
        <property name="minIdle" value="5"></property>
        <property name="maxActive" value="100"></property>

        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000"></property>

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000"></property>

        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="300000"></property>

        <property name="validationQuery" value="SELECT 'x'"></property>
        <property name="testWhileIdle" value="true"></property>
        <property name="testOnBorrow" value="false"></property>
        <property name="testOnReturn" value="false"></property>

        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="true"></property>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20"></property>

        <!-- 关闭长时间不使用的连接 -->
        <property name="removeAbandoned" value="false"></property> <!-- 打开removeAbandoned功能 -->
        <property name="removeAbandonedTimeout" value="1200"></property> <!-- 1200秒，也就是20分钟 -->
        <property name="logAbandoned" value="true"></property> <!-- 关闭abanded连接时输出错误日志 -->
    </bean>

    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource"></property>
        <property name="databaseSchemaUpdate" value="true"></property>
    </bean>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
        <property name="processEngineConfiguration" ref="processEngineConfiguration"></property>
    </bean>

</beans>
```

这篇文章学习了怎么在java中使用工作流，以及怎么集成到spring中，下一篇我将学习使用工作流的api接口。

