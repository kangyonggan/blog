---
title: 在jboss上配置jndi
date: 2018-08-06 13:45:08
categories: 系统运维
tags:
- Linux
---


## 准备工作
以配置MySQL的jndi为例。
1. jdk环境
2. jboss安装包
3. MySQL驱动包

## 修改配置
${JBOSS_HOME}/standalone/configuration/`standalone.xml`

<!-- more -->

```
<datasources>
    <datasource jndi-name="java:jboss/datasources/DemoDS" pool-name="DemoDS" enabled="true" use-java-context="true">
        <connection-url>jdbc:mysql://127.0.0.1:3306/demodb</connection-url>
        <driver>mysql</driver>
        <security>
            <user-name>root</user-name>
            <password>123456</password>
        </security>
    </datasource>
    <drivers>
        <driver name="mysql" module="com.kangyonggan.mysql">
            <xa-datasource-class>com.mysql.jdbc.Driver</xa-datasource-class>
        </driver>
    </drivers>
</datasources>
```

## 添加驱动
在${JBOSS_HOME}/modules/com/kangyonggan/mysql/main/目录下添加mysql驱动包，如：`mysql-connector-java-5.1.38.jar`, 这个jar包可以从本地maven仓库中找到，也可以从中央仓库下载。



在此目录下添加驱动包的配置文件：`module.xml`， 内容如下：

```
<?xml version="1.0" encoding="UTF-8"?>

<module xmlns="urn:jboss:module:1.1" name="com.kangyonggan.mysql">

    <resources>
        <resource-root path="mysql-connector-java-5.1.38.jar"></resource>
    </resources>
    <dependencies>
        <module name="javax.api"></module>
        <module name="javax.transaction.api"></module>
        <module name="javax.servlet.api" optional="true"></module>
    </dependencies>
</module>
```



