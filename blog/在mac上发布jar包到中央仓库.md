---
title: 在mac上发布jar包到中央仓库
date: 2018-08-06 13:45:08
categories: 综合
tags:
- Java
---

### 注册sonatype账号
地址：[https://issues.sonatype.org/secure/Signup!default.jspa](https://issues.sonatype.org/secure/Signup!default.jspa)

> * 记住用户名和密码，后面会频繁使用。

### 创建一个issue
地址：[https://issues.sonatype.org/secure/Dashboard.jspa](https://issues.sonatype.org/secure/Dashboard.jspa)
使用上面注册的用户名和密码登录。

<!-- more -->

![sona](/upload/article/sona-01.png)

> * 不要修改`Project`和`Issue Type`，使用默认值即可。

![sona](/upload/article/sona-02.png)

> * 其他的值请参考例子。

创建完成后，1个工作日以内工作人员会回复你的Issue，如果通过的话，内容如下，如果不通过，那你就检查后再试试吧。

![sona](/upload/article/sona-03.png)

### 使用 GPG 生成密钥对
如果mac上没有gpg命令，可以使用下面的命令进行安装:

```
brew install gpg
```

如果mac上没有brew命令，需要先安装brew命令：

```
ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)"
```

> * 安brew命令需要先打开Xcode并同意条款。

安装成功gpg命令后，生成密钥：

```
gpg --gen-key
```

输出信息：

```
gpg (GnuPG) 2.2.1; Copyright (C) 2017 Free Software Foundation, Inc.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.

gpg: directory '/Users/kyg/.gnupg' created
gpg: keybox '/Users/kyg/.gnupg/pubring.kbx' created
Note: Use "gpg --full-generate-key" for a full featured key generation dialog.

GnuPG needs to construct a user ID to identify your key.

Real name:
```

提示输入名字， 如：`kangyonggan`

```
Email address:
```

又提示输入邮箱, 如：`java@kangyonggan.com`

```
You selected this USER-ID:
    "kangyonggan <java@kangyonggan.com>"

Change (N)ame, (E)mail, or (O)kay/(Q)uit?
```

选择okay, 输入：`o`

然后弹框要求输入密码， 这个密码后面会用到，别忘记了。

![sona](/upload/article/sona-06.png)

之后会让你再次输入密码，到此密钥就生成成功了，输出信息如下：

```
We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
We need to generate a lot of random bytes. It is a good idea to perform
some other action (type on the keyboard, move the mouse, utilize the
disks) during the prime generation; this gives the random number
generator a better chance to gain enough entropy.
gpg: /Users/kyg/.gnupg/trustdb.gpg: trustdb created
gpg: key C38B01CFC194AE3C marked as ultimately trusted
gpg: directory '/Users/kyg/.gnupg/openpgp-revocs.d' created
gpg: revocation certificate stored as '/Users/kyg/.gnupg/openpgp-revocs.d/7A98F7517B453D562425F564C38B01CFC194AE3C.rev'
public and secret key created and signed.

pub   rsa2048 2017-10-18 [SC] [expires: 2019-10-18]
      7A98F7517B453D562425F564C38B01CFC194AE3C
uid                      kangyonggan <java@kangyonggan.com>
sub   rsa2048 2017-10-18 [E] [expires: 2019-10-18]
```

需要记住`7A98F7517B453D562425F564C38B01CFC194AE3C`，会面会有用，记不住也没关系，可以使用下面的命令查看。

```
gpg --list-keys
```

输出：

```
gpg: checking the trustdb
gpg: marginals needed: 3  completes needed: 1  trust model: pgp
gpg: depth: 0  valid:   1  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 1u
gpg: next trustdb check due at 2019-10-18
/Users/kyg/.gnupg/pubring.kbx
-----------------------------
pub   rsa2048 2017-10-18 [SC] [expires: 2019-10-18]
      7A98F7517B453D562425F564C38B01CFC194AE3C
uid           [ultimate] kangyonggan <java@kangyonggan.com>
sub   rsa2048 2017-10-18 [E] [expires: 2019-10-18]
```

### 将公钥发布到 PGP 密钥服务器
```
gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys 7A98F7517B453D562425F564C38B01CFC194AE3C
```

输出信息如下：

```
gpg: sending key C38B01CFC194AE3C to hkp://keyserver.ubuntu.com:11371
You have new mail in /var/mail/kyg
```

说明密钥已经发布成功了，也可以使用命令查看有没有发布成功。

```
gpg --keyserver hkp://keyserver.ubuntu.com:11371 --recv-keys 7A98F7517B453D562425F564C38B01CFC194AE3C
```

输出：

```
gpg: key C38B01CFC194AE3C: "kangyonggan <java@kangyonggan.com>" not changed
gpg: Total number processed: 1
gpg:              unchanged: 1
```

### 配置setting.xml
```
<servers>
    <server>
        <id>oss</id>
        <username>sonatype用户名</username>
        <password>sonatype密码</password>
    </server>
</servers>

...

<profiles>
    <profile>
        <id>ossrh</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>

        <properties>
            <gpg.executable>gpg</gpg.executable>
            <gpg.passphrase>密钥的密码</gpg.passphrase>
        </properties>
    </profile>
</profiles>
```

server的id随意填写，只要和pom.xml里面对应就行了。

### 配置pom.xml
```
<name>Method Logger</name>
<description>使用编译时注解打印方法入参出参和耗时</description>

<url>http://www.dexcoder.com/</url>
<licenses>
    <license>
        <name>The Apache Software License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
</licenses>
<developers>
    <developer>
        <name>kangyonggan</name>
        <email>java@kangyonggan.com</email>
    </developer>
</developers>
<scm>
    <connection>scm:git:git@github.com/kangyonggan/method-logger.git</connection>
    <developerConnection>scm:git:git@github.com/kangyonggan/method-logger.git</developerConnection>
    <url>git@github.com/kangyonggan/method-logger.git</url>
</scm>

<profiles>
    <profile>
        <id>release</id>
        <build>
            <plugins>
                <!-- Source -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-source-plugin</artifactId>
                    <version>2.2.1</version>
                    <executions>
                        <execution>
                            <phase>package</phase>
                            <goals>
                                <goal>jar-no-fork</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
                <!-- Javadoc -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-javadoc-plugin</artifactId>
                    <version>2.9.1</version>
                    <executions>
                        <execution>
                            <phase>package</phase>
                            <goals>
                                <goal>jar</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>

                <!-- GPG -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-gpg-plugin</artifactId>
                    <version>1.6</version>
                    <executions>
                        <execution>
                            <phase>verify</phase>
                            <goals>
                                <goal>sign</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
        <distributionManagement>
            <snapshotRepository>
                <id>oss</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
            </snapshotRepository>
            <repository>
                <id>oss</id>
                <url>https://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
    </profile>
</profiles>
```

如果你的英文很六，description标签最好用英文来写，逼格更高。其他值你看着填写就好。

> repository->id 要和 setting.xml 中的保持一致。

### 上传构件到 OSS 中
```
mvn clean deploy -P release
```

当执行以上 Maven 命令后，如果顺利，就会看见一堆upload信息，部分输出如下：

```
[DEBUG] Using transporter WagonTransporter with priority -1.0 for https://oss.sonatype.org/service/local/staging/deploy/maven2/
[DEBUG] Using connector BasicRepositoryConnector with priority 0.0 for https://oss.sonatype.org/service/local/staging/deploy/maven2/ with username=java@kangyonggan.com, password=***
Uploading: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/1.0/method-logger-1.0.jar
Uploaded: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/1.0/method-logger-1.0.jar (14 KB at 3.6 KB/sec)
Uploading: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/1.0/method-logger-1.0.pom
Uploaded: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/1.0/method-logger-1.0.pom (6 KB at 1.2 KB/sec)
Downloading: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/maven-metadata.xml
[DEBUG] Could not find metadata com.kangyonggan:method-logger/maven-metadata.xml in oss (https://oss.sonatype.org/service/local/staging/deploy/maven2/)
[DEBUG] Writing tracking file /Users/kyg/data/repository/com/kangyonggan/method-logger/resolver-status.properties
Uploading: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/maven-metadata.xml
Uploaded: https://oss.sonatype.org/service/local/staging/deploy/maven2/com/kangyonggan/method-logger/maven-metadata.xml (304 B at 0.2 KB/sec)
```

> * 注意：此时上传的构件并未正式发布到中央仓库中，只是部署到 OSS 中了，下面才是真正的发布

### 发布构建
地址：[https://oss.sonatype.org/#stagingRepositories](https://oss.sonatype.org/#stagingRepositories)
需要使用之前注册的sonatype账号登录。

点击`Staging Repositories`菜单，并拉至最下方，会看见自己刚刚deploy的jar包

![sona](/upload/article/sona-07.png)

选择此构建后，点击`Close`，如下图：

![sona](/upload/article/sona-08.png)

系统会自动验证该构件是否满足指定要求，当验证完毕后，状态会变为 Closed。然后点击`Release`。

### 通知 Sonatype 构件已成功发布
找到之前创建的Issue，并回复：`构建已发布`

![sona](/upload/article/sona-04.png)

### 等待构件审批通过
等待大概一个工作日内，如果审核通过，Issue会有回复：

![sona](/upload/article/sona-05.png)

> * 大概需要2个小时才能把jar同步到中央仓库。

### 从中央仓库搜索自己发布的jar
地址：[http://mvnrepository.com/](http://mvnrepository.com/)
搜索,如：`com.kangyonggan`, 结果如下：

![sona](/upload/article/sona-09.png)

据我实际测试发现，大概release之后不超过20分钟，就可以在[https://repo.maven.apache.org/maven2/](https://repo.maven.apache.org/maven2/)这里查看到了，只要这里有了，别人就可以依赖这个jar了。

> * 二次发布同一个构建（只要groupId不变）的过程就不用这么麻烦，后面就不用再Issue中操作了。


