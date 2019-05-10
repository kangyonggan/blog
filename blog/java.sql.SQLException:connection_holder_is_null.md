---
title: java.sql.SQLException:connection_holder_is_null
date: 2018-08-06 13:45:08
categories: 数据库
tags:
- MySQL
---


## 异常信息
```
java.sql.SQLException: connection holder is null
```

## 相关配置
```
<!-- 关闭长时间不使用的连接 -->
<property name="removeAbandoned" value="true" /> <!-- 打开removeAbandoned功能 -->
<property name="removeAbandonedTimeout" value="1200" /> <!-- 1200秒，也就是20分钟 -->
<property name="logAbandoned" value="true" /> <!-- 关闭abanded连接时输出错误日志 -->
```

## 解决方案
1. removeAbandoned 设置为 false，不建议。
2. removeAbandonedTimeout 延长这个过期时间，也不是太建议。
3. 指定不要关闭某个方法的连接（how？）