---
title: 学习SpringCloud07-使用docker部署SpringCloud项目
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---


## 文档
- [https://blog.csdn.net/forezp/article/details/70198649](https://blog.csdn.net/forezp/article/details/70198649)
- [http://www.runoob.com/docker/docker-hello-world.html](http://www.runoob.com/docker/docker-hello-world.html)
- [https://blog.csdn.net/lvyuan1234/article/details/69255944](https://blog.csdn.net/lvyuan1234/article/details/69255944)

## 安装docker
各平台docker下载地址：[http://get.daocloud.io/](http://get.daocloud.io/)

<!-- more -->

### docker容器常用命令

```
# 查看docker的版本号
kangyonggans-MacBook-Pro:~ kyg$ docker --version
Docker version 1.12.3, build 6b644ec

# 查看docker的基本信息
kangyonggans-MacBook-Pro:~ kyg$ docker version
Containers: 3
 Running: 1
 Paused: 0
 Stopped: 2
Images: 10
Server Version: 1.12.3
...

# 运行一个容器[-d后台运行，-p端口号，-t容器名称or镜像ID]
kangyonggans-MacBook-Pro:~ kyg$ docker run -d -p 8761:8761 -t sc02-eureka-server:0.0.1-SNAPSHOT
d501ba7a268c79492c176b232e69290ba32fcf47fb4dba2668136880b0c49c5d

## 查看正在运行的容器
kangyonggans-MacBook-Pro:~ kyg$ docker ps
CONTAINER ID        IMAGE                               COMMAND                  CREATED             STATUS              PORTS                    NAMES
cd4d076d8a7e        sc02-eureka-server:0.0.1-SNAPSHOT   "java -Djava.security"   9 minutes ago       Up 9 minutes        0.0.0.0:8761->8761/tcp   condescending_mcclintock

## 查看容器所使用的端口
kangyonggans-MacBook-Pro:~ kyg$ docker port cd4d076d8a7e
8761/tcp -> 0.0.0.0:8761

## 查看指定的容器ID的[实时]日志
docker logs -f cd4d076d8a7e

## 查看容器的进程[PID]
kangyonggans-MacBook-Pro:~ kyg$ docker top cd4d076d8a7e
PID                 USER                TIME                COMMAND
2197                root                0:34                java -Djava.security.egd=file:/dev/./urandom -jar /app.jar

# 查看容器的底层信息
kangyonggans-MacBook-Pro:~ kyg$ docker inspect cd4d076d8a7e
[
    {
        "Id": "cd4d076d8a7e286c18cdc9efddb3923d1c02c3989fa475436e2996fb9fac0345",
        "Created": "2018-06-21T06:22:07.34160656Z",
        "Path": "java",
        "Args": [
            "-Djava.security.egd=file:/dev/./urandom",
            "-jar",
            "/app.jar"
        ],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 2197,
...

# 停止容器
kangyonggans-MacBook-Pro:~ kyg$ docker stop cd4d076d8a7e
cd4d076d8a7e

# 启动容器
kangyonggans-MacBook-Pro:~ kyg$ docker start cd4d076d8a7e
cd4d076d8a7e

# 移除容器[-f可移除运行中的容器]
kangyonggans-MacBook-Pro:~ kyg$ docker rm -f cd4d076d8a7e
cd4d076d8a7e
```

### docker镜像常用命令
```
# 列出镜像列表
kangyonggans-MacBook-Pro:~ kyg$ docker images
REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
sc02-eureka-server   0.0.1-SNAPSHOT      a7a90edc7d8d        31 minutes ago      732.6 MB
<none>               <none>              93487316dcfd        34 minutes ago      732.6 MB
java                 8                   d23bdf5b1b1b        17 months ago       643.2 MB

# 获取一个新的镜像
kangyonggans-MacBook-Pro:~ kyg$ docker pull ubuntu:13.10

# 查找镜像
kangyonggans-MacBook-Pro:~ kyg$ docker search eureka

# 构建镜像[docker-maven-plugin]
docker build

```

## 修改`eureka-server`项目
### pom.xml
```
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <imageName>${project.name}:${project.version}</imageName>
        <dockerDirectory>${project.basedir}/src/main/docker</dockerDirectory>
        <skipDockerBuild>false</skipDockerBuild>
        <resources>
            <resource>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>
```

### 创建`Dockerfile`
1. 在`src/main`目录下创建目录`docker`
2. 在`src/main/docker`目录下创建文件`Dockerfile`和`runboot.sh`
3. `Dockerfile`文件内容如下：

```
# 基于哪个镜像
FROM java:8

# 将本地文件夹挂载到当前容器
VOLUME /tmp

# 拷贝文件到容器，也可以直接写成ADD eureka-server-0.0.1-SNAPSHOT.jar /app.jar
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'

# 开放8761端口
EXPOSE 8761

# 配置容器启动后执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

### 构建镜像
```
mvn package docker:build
```

### 运行镜像
```
docker run -d -p 8761:8761 -t eureka-server:0.0.1-SNAPSHOT
```

### 测试
访问：[http://localhost:8761/](http://localhost:8761/)

## 采用docker-compose启动镜像
在项目根目录下创建文件`docker-compose.yml`: 

```
eureka-server:
  image: eureka-server:0.0.1-SNAPSHOT
  restart: always
  ports:
    - 8761:8761
```

然后再执行命令即可启动：

```
docker-compose up
```