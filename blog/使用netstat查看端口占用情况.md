---
title: 使用netstat查看端口占用情况
date: 2018-08-06 13:45:08
categories: 系统运维
tags:
- Linux
---



## 参数
参数   |     全拼    |       说明
----- | ------------ | -------------
-a   | –all  | 显示所有连线中的Socket
-c   | –continuous  | 持续列出网络状态
-C   | –cache  | 显示路由器配置的快取信息
-e   | –extend  | 显示网络其他相关信息
-F   | –fib  | 显示FIB
-g   | –groups  | 显示多重广播功能群组组员名单
-i   | –interfaces  | 显示网络界面信息表单
-l   | –listening  | 显示监控中的服务器的Socket
-M   | –masquerade  | 显示伪装的网络连线
-n   | –numeric  | 直接使用IP地址，而不通过域名服务器
-N   | ––netlink或–symbolic  | 显示网络硬件外围设备的符号连接名称
-o   | –timers  |  显示计时器
-p   | –programs  | 显示正在使用Socket的程序识别码和程序名称
-r   | –route  | 显示Routing Table
-s   | –statistice  | 显示网络工作信息统计表
-t   | –tcp  | 显示TCP传输协议的连线状况
-u   | –udp  | 显示UDP传输协议的连线状况
-v   | –verbose  | 显示指令执行过程
-V   | –version  | 显示版本信息
-w   | –raw  | 显示RAW传输协议的连线状况
-x   | –unix  | 此参数的效果和指定”-A unix”参数相同
-ip   | –inet  | 此参数的效果和指定”-A inet”参数相同

## 实例
### 查看端口占用情况
```
sudo netstat -tunlp | grep 6379
```

输出：

```
tcp        0      0 0.0.0.0:6379            0.0.0.0:*               LISTEN      1962/redis-server *
tcp6       0      0 :::6379                 :::*                    LISTEN      1962/redis-server *
```