---
title: 性能测试工具jmeter
date: 2018-08-28 09:55:05
categories: Java后台
tags:
- Java
---


## 简介
Apache JMeter是Apache组织开发的基于Java的压力测试工具。用于对软件做压力测试，它最初被设计用于Web应用测试，但后来扩展到其他测试领域。 它可以用于测试静态和动态资源，例如静态文件、Java 小服务程序、CGI 脚本、Java 对象、数据库、FTP 服务器， 等等。JMeter 可以用于对服务器、网络或对象模拟巨大的负载，来自不同压力类别下测试它们的强度和分析整体性能。另外，JMeter能够对应用程序做功能/回归测试，通过创建带有断言的脚本来验证你的程序返回了你期望的结果。为了最大限度的灵活性，JMeter允许使用正则表达式创建断言。
Apache jmeter 可以用于对静态的和动态的资源（文件，Servlet，Perl脚本，java 对象，数据库和查询，FTP服务器等等）的性能进行测试。它可以用于对服务器、网络或对象模拟繁重的负载来测试它们的强度或分析不同压力类型下的整体性能。你可以使用它做性能的图形分析或在大并发负载测试你的服务器/脚本/对象。

## 作用
1. 能够对HTTP和FTP服务器进行压力和性能测试， 也可以对任何数据库进行同样的测试（通过JDBC）。
2. 完全的可移植性和100% 纯java。
3. 完全 Swing 和轻量组件支持（预编译的JAR使用 javax.swing.*)包。
4. 完全多线程 框架允许通过多个线程并发取样和 通过单独的线程组对不同的功能同时取样。
5. 精心的GUI设计允许快速操作和更精确的计时。
6. 缓存和离线分析/回放测试结果。

<!-- more -->

## 名词解释
1. Label： 定义的HTTP请求名称
2. Samples： 表示这次测试中一共发出了多少个请求
3. Average： 访问页面的平均响应时间
4. Min: 访问页面的最小响应时间
5. Max: 访问页面的最大响应时间
6. Error%： 错误的请求的数量/请求的总数
7. Throughput：每秒完成的请求数
8. KB/Sec： 每秒从服务器端接收到的数据量

## 下载地址
[http://jmeter.apache.org/download_jmeter.cgi](http://jmeter.apache.org/download_jmeter.cgi)

## 安装使用
解压下载的安装包，可以在bin目录下找到启动命令`jmeter.sh`或`jmeter.bat`，运行启动命令即可启动。

启动界面如下图：
![](/upload/article/jmeter-01.png)

## 使用
比如我们有一个接口需要压测。接口是http协议，post请求，地址是http://localhost:8080/login, 参数为"username=admin&password=123456"

### 创建测试计划
![](/upload/article/jmeter-02.png)

### 添加线程组
![](/upload/article/jmeter-03.png)
![](/upload/article/jmeter-04.png)

### 创建请求
![](/upload/article/jmeter-05.png)
![](/upload/article/jmeter-06.png)

到这里我们已经可以运行刚刚创建的请求了，但是对于请求结果及分析我们还没地方观察，因此我们还需要创建一些监听器，用于观察及分析结果。

### 观察结果树
![](/upload/article/jmeter-07.png)
![](/upload/article/jmeter-08.png)

### 聚合报告
![](/upload/article/jmeter-09.png)
![](/upload/article/jmeter-10.png)

至此一个简单的压测就搭建好了，可以运行一下试试了，运行之后就可以在观察结果数和聚合报告中查看、分析接口性能了。

![](/upload/article/jmeter-11.png)
![](/upload/article/jmeter-12.png)

更多高级用法请自行研究。