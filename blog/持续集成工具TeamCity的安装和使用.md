---
title: 持续集成工具TeamCity的安装和使用
date: 2018-08-06 13:45:08
categories: 综合
tags:
- Java
---

## 概述
TeamCity是一款功能强大的持续集成（Continue Integration）工具。

和IntelliJ IDEA同属于JetBrains公司，因此，TeamCity可以和IDEA无缝集成，通俗一点说，就是可以在IDEA上一键发布。

官方地址：[https://www.jetbrains.com/teamcity/](https://www.jetbrains.com/teamcity/)
官方文档：[https://confluence.jetbrains.com/display/TCD9/TeamCity+Documentation](https://confluence.jetbrains.com/display/TCD9/TeamCity+Documentation)

<!-- more -->

## 准备工作
1、windows 或 Mac 或 Linux
2、配置`JAVA_HOME`环境变量
3、下载安装包[https://www.jetbrains.com/teamcity/download/](https://www.jetbrains.com/teamcity/download/)

## 安装说明
以mac系统为例, 我下载的安装包为`TeamCity-2017.2.1.tar.gz`。

### 解压到指定目录
```
tar -zxvf /Users/kyg/Downloads/TeamCity-2017.2.1.tar.gz -C /Users/kyg/install/
```

### 启动和停止
启动

```
sh /Users/kyg/install/TeamCity/bin/runAll.sh start
```

停止

```
sh /Users/kyg/install/TeamCity/bin/runAll.sh stop
```

## 访问
[http://127.0.0.1:8111](http://127.0.0.1:8111)

TeamCity内置了一个tomcat容器，默认端口为8111，可以在
`/Users/kyg/install/TeamCity/conf/server.xml`修改端口。

```
<Connector port="8111" ...
```

首次访问，会让你选择存放配置的目录， 如下图：

![](/upload/article/tc-01.png)

默认即可，点击`Proceed`， 接下来是选择数据库，如下图：

![](/upload/article/tc-02.png)

这里我选择的是mysql，选择mysql后，需要把mysql的驱动包(可以从本地maven仓库中找到)拷贝到
`/Users/kyg/.BuildServer/lib/jdbc`目录下，然后点击`Refresh JDBC Drivers`, 如果jar包没问题，会出现下面的界面：

![](/upload/article/tc-03.png)

最后填写jdbc连接的相关信息后点击`Proceed`，需要先自己创建一个数据库`teamcity`
下一步就是接受条款，不多说。

然后就是让我们创建一个管理员, 如下图：

![](/upload/article/tc-04.png)

创建完成后会直接登录，并让我们补全个人信息，如下图：

![](/upload/article/tc-05.png)

到此，TeamCity就算安装完毕了，接下来就是配置一个项目，用于持续集成。

## 创建项目
![](/upload/article/tc-06.png)

此处我使用的是github上的项目。
![](/upload/article/tc-07.png)

![](/upload/article/tc-08.png)

如果是maven项目，它会自动识别出，并推荐你使用它的构建步骤，这里选不选都行，因为回头可以修改，基本上必须修改。
![](/upload/article/tc-09.png)

![](/upload/article/tc-10.png)

勾选后，跳转到构建步骤界面，这里你可以自由配置自己的构建步骤，比如
第一步：更新代码并打包
第二步：把war包拷贝到服务器
第三步：执行几个shell命令，停止服务+替换war包+启动服务

下面是我的配置，可供参考：
![](/upload/article/tc-11.png)

以及三个步骤的具体配置：
![](/upload/article/tc-12.png)

![](/upload/article/tc-13.png)

![](/upload/article/tc-14.png)

接下来就可以尝试启动了，点击右上角的`run`， 如果运气好，就会发布成功。

这个过程会很慢甚至会失败，因为你没有配置maven的`settings.xml`
它可能找不到你的仓库，如果你用的全部都是中央仓库的jar那就没问题

为了保证一个服务器只有一个本地仓库，最好指定一下settings.xml
如下图：
![](/upload/article/tc-15.png)

到此，算是搞定了，但是它还有另外一种操作，那就是集成在IDEA中。

## 集成到IDEA
在idea中搜索插件`teamcity`，安装后重启。
![](/upload/article/tc-16.png)

重启后，会发现下面3点变化，右下角的那个图标表示暂未登录到TeamView。
![](/upload/article/tc-17.png)

点击右下角的图标登录TeamView：
![](/upload/article/tc-18.png)

登录成功后，即可在IDEA中一键发布项目了。
![](/upload/article/tc-19.png)

这样的话，一个IDEA就可以开发代码，运行项目，控制版本，操作数据库，一键发布，强大的不要不要的。
