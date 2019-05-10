---
title: SpringBoot完美集成Freemarker
date: 2018-08-06 18:46:03
---


## 依赖
```
<!--freemarker-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
<dependency>
    <groupId>com.kangyonggan</groupId>
    <artifactId>freemarker</artifactId>
    <version>1.0.0</version>
</dependency>
```

其中`com.kangyonggan.freemarker`是我发布到中央仓库的一个jar。

<!-- more -->

## 配置
`application.yml`:  
```
spring:
  freemarker:
    settings:
      auto_import: common.ftl as c
```

spring-boot是很讲究零配置的，所以Freemarker大部分配置都已经被默认配置好了，完美只需要配置一些自定义的即可，如上面的配置，我想配置一个自动导入（通用）模板。

## 集成、覆写等
```
package com.kangyonggan.ck.config;

import com.kangyonggan.freemarker.BlockDirective;
import com.kangyonggan.freemarker.ExtendsDirective;
import com.kangyonggan.freemarker.OverrideDirective;
import com.kangyonggan.freemarker.SuperDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author kangyonggan
 * @since 8/6/18
 */
@Configuration
public class FreemarkerConfiguration {

    @Autowired
    freemarker.template.Configuration configuration;

    @PostConstruct
    public void setSharedVariable() {
        configuration.setSharedVariable("block", new BlockDirective());
        configuration.setSharedVariable("override", new OverrideDirective());
        configuration.setSharedVariable("extends", new ExtendsDirective());
        configuration.setSharedVariable("super", new SuperDirective());
    }

}
```

这时候就可以在模板中使用`<#include "footer.ftl"/>`、`<@block name="main"/>`、`<@extends name="layout.ftl"/>`指令了。

## 热加载
当我们修改了ftl界面内容后，springboot默认是不会进行热加载的，我们需要对idea动手脚。  

![](/upload/article/freemarker-01.png)

![](/upload/article/freemarker-02.png)

![](/upload/article/freemarker-03.png)

当配置好这两个之后，修改ftl保存之后，刷新界面就会加载了。



