---
title: Rabbitmq简记
date: 2019-03-28 13:00:49
categories: 系统运维
tags:
- Linux
---

## 安装
```
# 安装
sudo apt-get install rabbitmq-server

# 启动
sudo rabbitmq-server start

# 停止
sudo rabbitmqctl stop

## 状态
sudo rabbitmqctl status
```

<!-- more -->

## 启用Web
```
sudo rabbitmq-plugins enable rabbitmq_management
```

## 访问Web
[http://127.0.0.1:15672](http://127.0.0.1:15672)

guest/guest