---
title: IntelliJ IDEA集开发、持续集成、代码质量检测和代码审核于一体(一)
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## 下载地址
- IntelliJ IDEA下载地址：[https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)
- TeamCity下载地址：[https://www.jetbrains.com/teamcity/download](https://www.jetbrains.com/teamcity/download)
- Upsource下载地址：[https://www.jetbrains.com/upsource/download](https://www.jetbrains.com/upsource/download)
- TeamCity Plugin：[https://plugins.jetbrains.com/plugin/1820-teamcity-integration](https://plugins.jetbrains.com/plugin/1820-teamcity-integration)
- Upsource Pligin：[https://plugins.jetbrains.com/plugin/7431-upsource-integration](https://plugins.jetbrains.com/plugin/7431-upsource-integration)
- 阿里巴巴代码质量检测插件：[https://plugins.jetbrains.com/plugin/10046-alibaba-java-coding-guidelines](https://plugins.jetbrains.com/plugin/10046-alibaba-java-coding-guidelines)

后面三个是idea插件，如果电脑能联网，可以在插件中搜索到直接安装，如果是公司内网，可以下载离线安装包，然后手动安装idea插件。

安装IDEA插件方法：`Preferences > Plugins > Install plugin from disk...`

<!-- more -->

## TeamCity的安装

> 以下都是以我博客服务器（ubuntu 16.04.4）为例

### 解压到指定目录
```
tar -zxvf soft/TeamCity-2017.2.3.tar.gz  -C install/
```

### 启动
```
sh install/TeamCity/bin/runAll.sh start
```

### 停止
```
sh install/TeamCity/bin/runAll.sh stop
```

### 访问
[http://kangyonggan.com:8111](http://kangyonggan.com:8111)


TeamCity内置了一个tomcat容器，默认端口为8111，可以在
`/home/hxzq/install/TeamCity/conf/server.xml`修改端口。

```
<Connector port="8111" ...
```

![](/upload/article/ARTICLE20180401b89693552301f2e1d60c3a780427f167a4738378.png)

默认即可，点击`Proceed`， 接下来是选择数据库，如下图：

![](/upload/article/ARTICLE201804014eb4101e00e74b27246e5ad2c387ebc2b149c35b.png)

这里我选择的是mysql，选择mysql后，需要把mysql的驱动包(可以从本地maven仓库中找到)拷贝到
`/home/hxzq/.BuildServer/lib/jdbc`目录下，然后点击`Refresh JDBC Drivers`, 如果jar包没问题，会出现下面的界面：

```
cp mysql-connector-java-5.1.34.jar /home/hxzq/.BuildServer/lib/jdbc/
```

![](/upload/article/ARTICLE2018040187bd260ade064bf05398f4c6a05612a27606d408.png)

最后填写jdbc连接的相关信息后点击`Proceed`，需要先自己创建一个数据库`teamcity`
下一步就是接受条款，不多说。

```
DROP DATABASE IF EXISTS teamcity;

CREATE DATABASE teamcity
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;
```

然后就是让我们创建一个管理员, 如下图：

![](/upload/article/ARTICLE2018040192a3a27d4ed5cc2c1fe4b149c8d1a57e8122a2e3.png)

创建完成后会直接登录，并让我们补全个人信息，如下图：

![](/upload/article/ARTICLE20180401b2cbe1b9fbf4274961a274da47d9daf9ec109958.png)

到此，TeamCity就算安装完毕了，接下来就是配置一个项目，用于持续集成。

## 创建项目
![](/upload/article/ARTICLE2018040164a3c5faec96dfedb57c97f0b0f7515353ec7bde.png)

此处我使用的是github上的项目。
![](/upload/article/ARTICLE2018040114db8b0e9e377d0f3d04b5641a351780877ba0e9.png)

![](/upload/article/ARTICLE2018040190c432c5b896215b65196e70be7329b53479978a.png)

如果是maven项目，它会自动识别出，并推荐你使用它的构建步骤，这里选不选都行，因为回头可以修改，基本上必须修改。
![](/upload/article/ARTICLE20180401945610b306ddc518538f8b34fd225bb99ca0b047.png)

勾选后，跳转到下面的构建步骤界面，这里你可以自由配置自己的构建步骤

![](/upload/article/ARTICLE2018040119397adc0f74429b8bfd0ebbe8bb2a3e636c6e43.png)

比如我配置的三个构建步骤：  
- 第一步：更新代码并打包
- 第二步：把war包拷贝到服务器
- 第三步：执行几个shell命令，停止服务+替换war包+启动服务

### 配置第一步
点击`Edit`
![](/upload/article/ARTICLE2018040142d927ce6004bb654d9e2da17b9ddc413604006f.png)

![](/upload/article/ARTICLE2018040169fed61178e0e18087a094c631c280880dbdf920.png)

如果配置的settings.xml不管用，可以在下面上传settings.xml文件

![](/upload/article/ARTICLE20180401ab7f2e264df5a3e87c93d11c1104da6cb39c7ef3.png)

## 配置第二步
点击`Add build setup`

![](/upload/article/ARTICLE201804010377f4258ce46f3fb1ef2c6f93ea0928a70f45b8.png)

![](/upload/article/ARTICLE2018040157a4a890be0e9580e47679a58a504c31b024f2d3.png)

如果不知道，第一步打出的war包在什么路径，可以先随意写几个字符，后面尝试运行时，会打印第一步的日志，从日志中可以看到war包路径。

接下来就可以尝试启动了，点击右上角的`run`， 注意看日志，顺便看看第一步打的war包的路径。

> 首次构建，时间会比较久，需要耐心等待

![](/upload/article/ARTICLE20180401ea6ffbce26284267db7f2cc11729e6144aab803d.png)

### 配置第三步
点击`Add build setup`

![](/upload/article/ARTICLE201804016153dda66dbc3992f894a5ca779771ad5cb1f513.png)

> 我再第一步打的war包路径为`/home/hxzq/install/TeamCity/buildAgent/work/23d0c25675dc1f18/blog-web/target/blog-web-1.0-SNAPSHOT.war`，可供参考。

其中ssh命令如下：

```
sh /home/hxzq/install/apache-tomcat-8.5.6-blog/bin/shutdown.sh

rm -rf /home/hxzq/install/apache-tomcat-8.5.6-blog/webapps/ROOT/*

unzip -d /home/hxzq/install/apache-tomcat-8.5.6-blog/webapps/ROOT/ /home/hxzq/data/wars/blog-*.war 

ln -s /home/hxzq/data/blog/upload/ /home/hxzq/install/apache-tomcat-8.5.6-blog/webapps/ROOT/WEB-INF/
ln -s /home/hxzq/data/blog/cover/ /home/hxzq/install/apache-tomcat-8.5.6-blog/webapps/ROOT/WEB-INF/
ln -s /home/hxzq/data/blog/rss/ /home/hxzq/install/apache-tomcat-8.5.6-blog/webapps/ROOT/WEB-INF/

sh /home/hxzq/install/apache-tomcat-8.5.6-blog/bin/startup.sh

ps -ef | grep tomcat
```

配置完成后界面如下：
![](/upload/article/ARTICLE201804019b2233e08610bf055fa4767a0af395580886ad2a.png)

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

但是！如果代码没有改变，是不能remote run的，我也没有找到怎么在没改变代码的情况下运行。


