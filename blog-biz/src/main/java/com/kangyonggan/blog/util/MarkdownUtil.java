package com.kangyonggan.blog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2019-06-03
 */
public final class MarkdownUtil {

    private MarkdownUtil() {
    }

    /**
     * 生成markdown目录
     *
     * @param content md语法的内容
     * @return
     */
    public static List<Map<String, Object>> genTocs(String content) {
        List<Map<String, Object>> docs = new ArrayList<>();

        String[] lines = content.split("\n");

        List<Map<String, Object>> childrenOne = null;
        List<Map<String, Object>> childrenTwo = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            // 判断目录级别
            if (line.startsWith("## ")) {
                String name = (docs.size() + 1) + ". " + line.substring(3);
                String link = line.substring(3).replaceAll("\\.", "");
                childrenOne = new ArrayList<>();

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", childrenOne);
                docs.add(header);
            } else if (line.startsWith("### ")) {
                if (childrenOne == null) {
                    continue;
                }
                String name = docs.size() + "." + (childrenOne.size() + 1) + ". " + line.substring(4);
                String link = line.substring(4).replaceAll("\\.", "");
                childrenTwo = new ArrayList<>();

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", childrenTwo);
                childrenOne.add(header);
            } else if (line.startsWith("#### ")) {
                if (childrenTwo == null) {
                    continue;
                }
                String name = docs.size() + "." + childrenOne.size() + "." + (childrenTwo.size() + 1) + ". " + line.substring(5);
                String link = line.substring(5).replaceAll("\\.", "");

                Map<String, Object> header = new HashMap<>(8);
                header.put("name", name);
                header.put("link", link);
                header.put("children", new ArrayList<>());
                childrenTwo.add(header);
            }

        }

        return docs;
    }

    public static void main(String[] args) {
        String content = "\n" +
                "## 回顾\n" +
                "![](https://kangyonggan.com/upload/blog/rpc.png)\n" +
                "由rpc主调用过程，我们第一步应该要搭建一个注册中心，我使用的注册中心是zookeeper，搭建过程自行百度，我搭建的注册中心地址：`122.112.204.190:2181`。\n" +
                "\n" +
                "有了注册中心，我们就可以开发一个服务提供者，然后在xml中配置注册中心的地址，才能把服务发布到注册中心。这其中有3点需要我们开发：\n" +
                "1. 自定义xml标签\n" +
                "2. 启动服务端\n" +
                "3. 发布服务\n" +
                "\n" +
                "本篇实现第3点！\n" +
                "\n" +
                "## 需求\n" +
                "在xml中配置一个User服务和一个注册中心。\n" +
                "```\n" +
                "<!-- 注册中心 -->\n" +
                "<rpc:register type=\"zookeeper\" ip=\"122.112.204.190\" port=\"2181\" />\n" +
                "\n" +
                "<!-- User服务 -->\n" +
                "<rpc:service id=\"userService\" name=\"com.kangyonggan.rpc.service.UserService\" ref=\"userServiceImpl\" />\n" +
                "<bean id=\"userServiceImpl\" class=\"com.kangyonggan.rpc.service.impl.UserServiceImpl\" />\n" +
                "```\n" +
                "容器启动之后，rpc框架会读取xml并解析，然后把User服务发布到注册中心。\n" +
                "\n" +
                "## 实现源码\n" +
                "github: [https://github.com/kangyonggan/rpc.git](https://github.com/kangyonggan/rpc.git)\n" +
                "分支：03-register-service\n" +
                "\n" +
                "## 使用说明\n" +
                "### 第一步\n" +
                "下载源码\n" +
                "```\n" +
                "git clone https://github.com/kangyonggan/rpc.git\n" +
                "```\n" +
                "\n" +
                "### 第二步\n" +
                "切换分支\n" +
                "```\n" +
                "git 03-register-service\n" +
                "```\n" +
                "\n" +
                "### 第三步\n" +
                "编译源码\n" +
                "```\n" +
                "mvn clean install\n" +
                "```\n" +
                "\n" +
                "### 第四步\n" +
                "运行测试用例，`Rpc03RegisterServiceTest.testPublish()`\n" +
                "```\n" +
                "[INFO ] 2019-02-16 12:48:00,957 com.kangyonggan.rpc.core.RpcServer.run(RpcServer.java:32) : RPC服务端正在启动...\n" +
                "[INFO ] 2019-02-16 12:48:00,968 com.kangyonggan.rpc.util.ZookeeperClient.<init>(ZookeeperClient.java:34) : 正在连接zookeeper[122.112.204.190:2181]\n" +
                "[INFO ] 2019-02-16 12:48:01,089 com.kangyonggan.rpc.util.ZookeeperClient.<init>(ZookeeperClient.java:36) : 连接zookeeper[122.112.204.190:2181]成功\n" +
                "[INFO ] 2019-02-16 12:48:01,129 com.kangyonggan.rpc.pojo.Service.registerService(Service.java:100) : 服务发布成功:[/rpc/com.kangyonggan.rpc.service.UserService/provider/192.168.2.188_9203]\n" +
                "[INFO ] 2019-02-16 12:48:01,193 com.kangyonggan.rpc.core.RpcServer.run(RpcServer.java:53) : RPC服务端启动完成，监听【9203】端口\n" +
                "```\n" +
                "\n" +
                "### 第五步\n" +
                "使用zookeeper客户端命令查看一下是不是真的发布到zookeeper上了：\n" +
                "```\n" +
                "root@kyg:~/install/zookeeper-3.4.13# pwd\n" +
                "/root/install/zookeeper-3.4.13\n" +
                "sh bin/zkCli.sh -server 122.112.204.190:2181\n" +
                "# 一堆日志\n" +
                "[zk: 122.112.204.190:2181(CONNECTED) 0] ls /rpc\n" +
                "[com.kangyonggan.rpc.service.UserService]\n" +
                "```\n" +
                "\n" +
                "可以看出UserService已经发布到zookeeper上了。\n" +
                "\n" +
                "## 坑点\n" +
                "```\n" +
                "zkClient.writeData(path, data);\n" +
                "```\n" +
                "\n" +
                "zkClient在把data写入path的时候，会将data序列化，data对应我们代码中是Service.java，因此要给Service.java实现序列化。\n" +
                "\n" +
                "但是如果仅仅将Service.java实现序列化还是回报错，因此Service中有两个字段序列化会失败，即`logger`和`applicationContext`，所以我们序列化时要排除这两个字段，使用关键字`transient`。\n";
        System.out.println(genTocs(content));
    }

}
