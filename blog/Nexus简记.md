---
title: Nexus简记
date: 2019-03-26 18:55:24
categories: 系统运维
tags:
- Linux
---

## 安装

```
下载
wget https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.15.2-01-unix.tar.gz
# 解压
tar -zxvf nexus-3.15.2-01-unix.tar.gz -C ../install/
# 启动
./nexus start
# 停止
./nexus stop
```

<!-- more -->

## 反向代理
由于nexus安装在内网服务器上，外网无法访问，所以需要使用反向代理，配置如下：

### 内网穿透

```
ssh -C -f -N -g -R 8881:127.0.0.1:8081 root@kangyonggan.com
```

### 外网nginx的配置

```
# nexus
server {
        listen 8081;
				client_max_body_size 50m;
        location / {
                proxy_set_header Host www.kangyonggan.com:8081;
                proxy_pass http://localhost:8881;
        }
}

```

## 外网访问
http://kangyonggan.com:8081<br />admin/admin123

## 使用
### settings.xml
```
<servers>
    <server>
      <id>releases</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
    <server>
      <id>snapshots</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
  </servers>
```

### pom.xml

```
<distributionManagement>
    <repository>
        <id>releases</id>
        <name>nexus releases</name>
        <url>http://kangyonggan.com:8081/repository/maven-releases/</url>
    </repository>

    <snapshotRepository>
        <id>snapshots</id>
        <name>nexus snapshots</name>
        <url>http://kangyonggan.com:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

## 测试

```
mvn deploy
```
![image.png](https://cdn.nlark.com/yuque/0/2019/png/159622/1552886026067-4e52b799-922e-47b6-be18-32d06cd75ccb.png#align=left&display=inline&height=993&name=image.png&originHeight=993&originWidth=1680&size=83598&status=done&width=1680)<br />
