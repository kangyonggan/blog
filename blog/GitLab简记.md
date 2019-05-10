---
title: GitLab简记
date: 2019-03-26 18:44:45
categories: 系统运维
tags:
- Linux
---

## 安装
> Ubuntu 16.04.6 LTS

```
# 查看端口占用情况，kill掉占用80的进程，否则会502
netstat -anptl

sudo apt-get install curl openssh-server ca-certificates postfix
curl -sS https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | sudo bash
sudo apt-get install gitlab-ce

sudo gitlab-ctl reconfigure
sudo gitlab-ctl status
```

<!-- more -->

## 反向代理
由于gitlab安装在内网服务器上，外网无法访问，所以需要使用反向代理，配置如下：

### 内网GitLab的配置

```
sudo vi /etc/gitlab/gitlab.rb
```

1. 修改GitLab的url

```
external_url 'http://kangyonggan.com:8888'
```
1. 修改监听的端口

```
nginx['listen_port'] = 8888
```

### 内网穿透

```
ssh -C -f -N -g -R 8880:127.0.0.1:8888 root@kangyonggan.com
```


### 外网Nginx的配置

```
sudo vi /etc/nginx/sites-available/default
```

增加配置如下：

```
# gitlab
server {
        listen 8888;

        location / {
                proxy_pass http://localhost:8880;
        }
}
```

## 情景
假设公司有3个小组，分别为：
* 网关组
* 风控组

各小组成员分别为：
* 网关组：张大（组长）、张二、张三。
* 风控组：李大（组长）、李二、李三、李四。

各小组项目分别为：
* 网关组：proj1、proj2。
* 风控组：proj3、proj4。

## 管理员
使用管理员（root）登录之后，找到管理员区域（Admin Area）。
### 添加组
![gitlab-01.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789465544-f1abe8bd-2286-4fd7-bd9e-500c91c9832f.png#align=left&display=inline&height=376&name=gitlab-01.png&originHeight=969&originWidth=1920&size=45632&status=done&width=746)
#### New Group
![gitlab-02.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789573991-47f3ccdd-5c46-4923-8724-0c55ad026431.png#align=left&display=inline&height=376&name=gitlab-02.png&originHeight=969&originWidth=1920&size=80829&status=done&width=746)<br />![gitlab-03.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789580543-fad3d07f-8ce2-4fb2-9c87-1043410536bb.png#align=left&display=inline&height=376&name=gitlab-03.png&originHeight=969&originWidth=1920&size=57904&status=done&width=746)
### 禁用注册
如果开放注册，到时候将会看见五花大门的用户名和邮箱地址，为了统一，当有新人到来时统一分配。<br />![gitlab-04.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789697811-2337a312-93bb-4e40-a6ac-8d290c2c2038.png#align=left&display=inline&height=376&name=gitlab-04.png&originHeight=969&originWidth=1920&size=77542&status=done&width=746)<br />退出登录后将会发现没有注册界面了<br />![gitlab-07.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789939832-d179361a-ba45-4a7e-a70c-565c1481abb5.png#align=left&display=inline&height=376&name=gitlab-07.png&originHeight=969&originWidth=1920&size=39467&status=done&width=746)<br />
### 添加成员
![gitlab-05.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789825007-2c213c8b-fe1c-4231-99e4-b9120bf30e84.png#align=left&display=inline&height=376&name=gitlab-05.png&originHeight=969&originWidth=1920&size=58200&status=done&width=746)<br />![gitlab-06.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552789834676-f8bff8a2-6ef0-44e3-9b6f-f2afafb48446.png#align=left&display=inline&height=376&name=gitlab-06.png&originHeight=969&originWidth=1920&size=67307&status=done&width=746)<br />注意：组长允许创建项目，但不允许创建组。<br />![gitlab-14.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552791029245-2ece65ce-916b-4a43-a20a-6a66fef62a60.png#align=left&display=inline&height=376&name=gitlab-14.png&originHeight=969&originWidth=1920&size=61319&status=done&width=746)<br />普通成员既不允许创建项目，也不允许创建组。

![gitlab-08.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790133361-d308ca8a-c825-4ef2-86ee-d2c6d70b9674.png#align=left&display=inline&height=376&name=gitlab-08.png&originHeight=969&originWidth=1920&size=92158&status=done&width=746)

如果用户收不到重置密码的邮件，管理员可以对用户进行编辑，在编辑页修改密码<br />![gitlab-12.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790643288-df061888-af22-49fb-a7d5-0a57ceb3e663.png#align=left&display=inline&height=376&name=gitlab-12.png&originHeight=969&originWidth=1920&size=62152&status=done&width=746)<br />
### 分配用户到组
![gitlab-09.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790325799-33334f9d-370a-48a1-8ce7-eab6039dda36.png#align=left&display=inline&height=376&name=gitlab-09.png&originHeight=969&originWidth=1920&size=83202&status=done&width=746)<br />![gitlab-10.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790331815-9b1c2840-abb3-4eff-962d-099a862b2ed3.png#align=left&display=inline&height=376&name=gitlab-10.png&originHeight=969&originWidth=1920&size=92619&status=done&width=746)<br />![gitlab-11.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790402190-7a0368ac-767c-4173-a2f9-65223197ab19.png#align=left&display=inline&height=376&name=gitlab-11.png&originHeight=969&originWidth=1920&size=98381&status=done&width=746)
## 组长
首次登录会要求修改密码<br />![gitlab-13.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552790840742-0ea38e2e-24a7-4623-b971-00585bc6691a.png#align=left&display=inline&height=376&name=gitlab-13.png&originHeight=969&originWidth=1920&size=35709&status=done&width=746)
### 创建项目

![gitlab-16.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552791334980-830f5bf9-1eca-4a3a-9229-8868542078d2.png#align=left&display=inline&height=376&name=gitlab-16.png&originHeight=969&originWidth=1920&size=76871&status=done&width=746)<br />![gitlab-17.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552791335381-60b53863-9b0c-49a0-90c4-13b4fc573676.png#align=left&display=inline&height=376&name=gitlab-17.png&originHeight=969&originWidth=1920&size=79542&status=done&width=746)

说明：developer角色不能push代码到master分支，通常master分支可以作为生产分支，仅组长可以push，开发者可以在其他分支开发，最后由组长合并代码到master分支。
