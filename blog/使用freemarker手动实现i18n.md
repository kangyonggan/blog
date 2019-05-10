---
title: 使用freemarker手动实现i18n
date: 2019-01-14 10:27:15
categories: Java后台
tags:
- Java

---

## FTL中的调用效果
```
<@message "app.name"/>
<@message "app.name" local="zh_CN"/>
```

## 宏定义
```
<#macro message code local="">
    <#if local!="">
        ${i18n("msg", code, local)}
    <#else>
        ${i18n("msg", code, '${springMacroRequestContext.getLocale().language}_${springMacroRequestContext.getLocale().country}')}
    </#if>
</#macro>
```

<!-- more -->

## 标签
```
package com.kangyonggan.demo.freemarker;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author kangyonggan
 * @since 2018/12/13
 */
@Component
@Log4j2
public class I18nTag extends AbstractFunctionTag {

    private static Map<String, Properties> data = new HashMap<>();

    /**
     * 国际化
     *
     * @param arguments 参数
     * @return 返回UUID
     */
    public String msg(List arguments) throws IOException {
        if (!hasLessThreeArgs(arguments)) {
            throw new RuntimeException("国际化时没有指定参数");
        }
        String code = arguments.get(1).toString();
        String local = arguments.get(2).toString();

        Properties properties = getProperties(local);
        return properties.getProperty(code);
    }

    private Properties getProperties(String local) throws IOException {
        Properties properties = data.get(local);

        if (properties == null) {
            properties = loadProperties(local);
            data.put(local, properties);
        }

        return properties;
    }

    private Properties loadProperties(String local) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = this.getClass().getResourceAsStream("/messages_" + local + ".properties");
        } catch (Exception e) {
            inputStream = null;
        }

        if (inputStream == null) {
            log.info("没有{}的国际化文件，使用默认的", local);
            inputStream = this.getClass().getResourceAsStream("/messages.properties");
        }

        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        properties.load(reader);
        return properties;
    }

}
```

## properties文件
`messages.properties`：

```
app.name=dfjz
```

`messages_zh_CN.properties`：

```
app.name=东方骄子
```


