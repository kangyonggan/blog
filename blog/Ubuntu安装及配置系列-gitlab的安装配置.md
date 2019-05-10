---
title: Ubuntu安装及配置系列-gitlab的安装配置.md
date: 2018-09-02 21:42:16
categories: 系统运维
tags: 
- Linux
---

## 安装
```
sudo apt-get install gitlab-ce
```

## 启动、停止、重启
```
sudo gitlab-ctl start
sudo gitlab-ctl stop
sudo gitlab-ctl restart
```

## 修改root的默认密码
```
gitlab-rails console production
> user = User.where(id: 1).first
> user.password=12345678
> user.password_confirmation=12345678
> user.save!
> quit
```

## 登录
http://localhost/
