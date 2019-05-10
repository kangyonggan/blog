---
title: 配置dubbo多注册中心
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

参考dubbo官方文档[http://dubbo.io/User+Guide-zh.htm#UserGuide-zh-%E5%A4%9A%E6%B3%A8%E5%86%8C%E4%B8%AD%E5%BF%83](http://dubbo.io/User+Guide-zh.htm#UserGuide-zh-%E5%A4%9A%E6%B3%A8%E5%86%8C%E4%B8%AD%E5%BF%83)

## consumer.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
       http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <context:annotation-config></context:annotation>
    <dubbo:application name="ENGINE_HELPER"></dubbo:application>

    <dubbo:registry id="dev_address" address="zookeeper://10.199.101.211:8080?backup=10.199.101.212:2181,10.199.101.213:2181"></dubbo:registry>
    <dubbo:registry id="uat_address" address="zookeeper://10.199.105.204:2181?backup=10.199.105.203:2181,10.199.105.202:2181" default="false"></dubbo:registry>

    <dubbo:reference id="bankEngineServiceDev" cluster="failfast"
                     interface="com.shhxzq.fin.bankengine.service.BankEngineService"
                     lazy="true" version="1.2.0" check="false" timeout="100000" registry="dev_address"></dubbo:reference>

    <dubbo:reference id="bankEngineServiceUat" cluster="failfast"
                     interface="com.shhxzq.fin.bankengine.service.BankEngineService"
                     lazy="true" version="1.2.0" check="false" timeout="100000" registry="uat_address"></dubbo:reference>

</beans>
```



