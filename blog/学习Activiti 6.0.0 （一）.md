---
title: 学习Activiti 6.0.0 （一）
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## 一、activiti简介
### 1.1 百度百科
[https://baike.baidu.com/item/activiti](https://baike.baidu.com/item/activiti)

### 1.2 官方文档
[https://www.activiti.org/userguide/6.latest/index.html](https://www.activiti.org/userguide/6.latest/index.html)

### 1.3 下载地址
[http://activiti.org/download.html](http://activiti.org/download.html)

### 1.4 源码地址
[https://github.com/Activiti/Activiti](https://github.com/Activiti/Activiti)

<!-- more -->

## 二、快速开始
### 2.1 小试牛刀
- 解压`activiti-6.0.0.zip`, 把`activiti-6.0.0/wars/activiti-app.war`拷贝到tomcat的webapps目录下，并启动。
- 访问[http://localhost:8080/activiti-app/](http://localhost:8080/activiti-app/)
- 用户名：`admin`、密码：`test`

但是，activiti-app默认使用的是内存数据库`H2`, 当重启后，我们在UI界面做的任何操作都不会保留。所以有必要使用其他数据库，比如MySQL。

### 2.2 配置数据库
- 创建一个数据库`acti`

```
DROP DATABASE IF EXISTS acti;

CREATE DATABASE acti DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE acti;
```

- 执行初始化脚本
    - `activiti-6.0.0/database/create/activiti.mysql.create.identity.sql`
    - `activiti-6.0.0/database/create/activiti.mysql55.create.engine.sql`
    - `activiti-6.0.0/database/create/activiti.mysql55.create.history.sql`
 
- 修改连接数据库的配置`activiti-app.properties`

这里需要说明一下：
- 如果MySQL的版本号小于5.6, 需要执行上面提到的三个sql脚本。
- 如果MySQL的版本号大于等于5.6，可以不用执行mysql55的脚本，而是执行`activiti.mysql.create.engine.sql`和`activiti.mysql.create.history.sql`

```
vi apache-tomcat-8.5.6/webapps/activiti-app/WEB-INF/classes/META-INF/activiti-app/activiti-app.properties
```

配置如下：

```
datasource.driver=com.mysql.jdbc.Driver
datasource.url=jdbc:mysql://127.0.0.1:3306/acti?characterEncoding=UTF-8

datasource.username=root
datasource.password=123456

hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

- 重启即可生效。















