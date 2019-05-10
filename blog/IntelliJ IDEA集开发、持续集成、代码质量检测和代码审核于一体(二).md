---
title: IntelliJ IDEA集开发、持续集成、代码质量检测和代码审核于一体(二)
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## Upsource的安装
### 解压到指定目录
```
unzip soft/upsource-2017.3.2888.zip -d install/
```

### 启动
```
sh install/upsource-2017.3.2888/bin/upsource.sh start
```

### 停止
```
sh install/upsource-2017.3.2888/bin/upsource.sh stop
```

<!-- more -->

### 访问
[http://kangyonggan.com:8081/](http://kangyonggan.com:8081/)
upsource会从8080端口开始，找到一个不是正在使用的端口使用，比如说8080端口没在使用的话，他就会使用8080端口

![](/upload/article/itu-20.png)

配置应用地址和端口，保持默认即可

![](/upload/article/itu-21.png)

设置管理员密码

![](/upload/article/itu-22.png)

使用默认的60天免费授权码

![](/upload/article/itu-23.png)

接下来就是等待配置，可能需要几分钟的时间

![](/upload/article/itu-24.png)
![](/upload/article/itu-25.png)

加载完成之后，如下图：

![](/upload/article/itu-26.png)

点击右上角的`Log in...`，登录后提示你完善个人信息

![](/upload/article/itu-27.png)

当我填写邮箱后点击保存，他提示我邮箱没有验证，当我点击发送验证邮件时，又提示邮件通知被禁用了

下图是开启邮件通知和smtp的配置

![](/upload/article/itu-28.png)
![](/upload/article/itu-29.png)
![](/upload/article/itu-30.png)
![](/upload/article/itu-31.png)
![](/upload/article/itu-32.png)

配置好smtp并且开启邮件通知之后，再回到profile，重新发送一个校验邮件，收到邮件后，点击邮件中的链接即可。

![](/upload/article/itu-33.png)

补充好个人信息之后，切到`upsource`视图去创建一个项目，从而开始review之路。

![](/upload/article/itu-34.png)
![](/upload/article/itu-35.png)
![](/upload/article/itu-36.png)
![](/upload/article/itu-37.png)

## 集成到idea中
在idea中安装upsource插件
![](/upload/article/itu-38.png)

重启idea后会在右下角看见up图标，点击后会弹框让你输入upsource服务器的地址，输入后弹出浏览器登录界面。

![](/upload/article/itu-39.png)

如何审核代码呢？在idea中找到要审核的代码`行`，`右键 > upsource > 留言`

![](/upload/article/itu-40.png)

留言后，在代码行首可以看见图标，点击图标可以查看留言内容，并且可以对其进行评论或其他骚操作。

![](/upload/article/itu-41.png)

还有其他更多操作，请自行探索。

## 代码质量检测
代码质量检测是按照阿里巴巴开发规范来进行检测的，为此阿里还开发了对应的idea、eclipse插件。在idea中按照此插件如下图：

![](/upload/article/itu-42.png)

安装完成重启idea后，可以在tools中看到对应的菜单、右键菜单里也有对应的菜单，也可以使用快捷键。

![](/upload/article/itu-43.png)

![](/upload/article/itu-44.png)















