---
title: IntelliJ IDEA自动生成setter方法丢失is
date: 2018-08-06 13:45:08
categories: 综合
tags:
- Java
---

### 实体类
有一个实体类Demo，他有一个boolean类型的属性isUpdate。

### idea
如果使用idea自动生成setter方法如下：

```
public void setUpdate(boolean update) {
    isUpdate = update;
}
```

### 前端请求
```
$.post("xxxxx/update", {
    isUpdate: true
}, function(data,status){
    alert("数据: \n" + data + "\n状态: " + status);
});
```

<!-- more -->

### spring注入
我们在前端请求后台controller时，controller方法的参数是实体类Demo，如下：

```
@RequestMapping(value = "update", method = RequestMethod.POST)
public String update(Demo demo) {
    // ...
    System.out.println("isUpdate is " + demo.getIsUpdate());
    return null;
}
```

### 输出
```
isUpdate is false
```

说明spring在注入时找不到isUpdate对应的setter方法。


### eclipse
如果使用eclipse自动生成setter方法如下：

```
public void setIsUpdate(boolean isUpdate) {
    this.isUpdate = isUpdate;
}
```

换成eclipse之后，生成的setter就是好的了, 如果使用lombok插件自动生成setter也是好的

> 说明：只有属性是boolean或者Boolean时，并且以is开头的字段，使用idea自动生成setter时才会丢失is。


