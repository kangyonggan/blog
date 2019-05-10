---
title: 手写最简版Tomcat
date: 2019-02-13 14:43:23
categories: Java后台
tags:
- Java
---

## 简介
Tomcat 服务器是一个免费的开放源代码的Web 应用服务器，属于轻量级应用服务器，在中小型系统和并发访问用户不是很多的场合下被普遍使用。

## 目标
手动实现一个最简单版的tomcat，实现以下功能：  
1. 运行main方法后能监听8080端口。
2. 收到请求后把方法和路径打印出来。
3. 给客户端响应。

<!-- more -->

## 服务端代码
```
package com.kangyonggan.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author kangyonggan
 * @since 2019-02-13
 */
public class MyTomcat {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Tomcat已启动，端口为8080");
            while (true) {
                // 等待请求
                Socket socket = serverSocket.accept();
                // 处理请求
                dealSocket(socket);
            }
        } catch (IOException e) {
            System.out.println("Tomcat启动异常");
            e.printStackTrace();
        }
    }

    /**
     * 处理请求。
     * 1. 解析http协议
     * 2. 打印方法和路径
     * 3. 写响应
     *
     * @param socket
     */
    private static void dealSocket(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 读取http协议的第一行，例：GET /hello?name=tomcat HTTP/1.1
            String line = reader.readLine();
            if (line != null) {
                System.out.println("===================Request==================");
                String[] arr = line.split("\\s");
                System.out.println("请求方法：" + arr[0]);
                System.out.println("请求路径：" + arr[1]);
                System.out.println("===================Request==================");

                // 写响应
                System.out.println("===================Response==================");
                String respMsg = "Hello Tomcat";
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // 响应内容必须符合http协议
                String result = "HTTP/1.1\nContent-type:text/html\n\n" + respMsg;
                System.out.println(respMsg);
                writer.write(result);
                writer.flush();
                writer.close();
                System.out.println("===================Response==================\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

## 测试
打开浏览器访问：http://localhost:8080/hello?name=tomcat

页面响应内容为：
Hello Tomcat

下面是一个完整的请求头供参考：
```
GET /hello?name=tomcat HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,id;q=0.7,en-US;q=0.6
Cookie: Idea-52bd2c0d=07a72752-b112-4867-88bd-58492f6658f9; bdshare_firstime=1546588983975; Hm_lvt_6a4086b67794408d40551fa5c68a96d8=1547430617,1547779481,1548318195,1548820657
```