---
title: 把网站从http免费升级到https
date: 2018-08-06 13:45:08
categories: 综合
tags:
- Linux
---

## 申请免费SSL
打开[https://zerossl.com/](https://zerossl.com/), 点击【ONLINE TOOLS】

![https](/upload/article/https01.png)

点击【START】开始申请证书

<!-- more -->

![https](/upload/article/https02.png)

填写【邮箱】、【域名】、勾选【HTTP verification】、【Accept ZeroSSL TOS】和【Accept Let's Encrypt SA (pdf)】, 填写完成后点击【NEXT】

![https](/upload/article/https03.png)

此时会询问你是否也包含www前缀的域名，建议第一次玩的时候选择No，因为这样后面的校验是相对容易一些，等玩会了之后再选择Yes，我这里选择的是【Yes】。

![https](/upload/article/https04.png)

点击【下载】，然后点击【NEXT】。

![https](/upload/article/https05.png)

点击【下载】，然后点击【NEXT】。

![https](/upload/article/https06.png)

然后进入域名校验界面。

![https](/upload/article/https07.png)

这时候你需要在你的项目中放置两个文件，以供校验：
1. 文件路径为：http://kangyonggan.com/.well-known/acme-challenge/WxvjNvXfff_DGNzHeGhi6rDqMVjJQlcatSvO_1SdemE
2. 文件内容为：WxvjNvXfff_DGNzHeGhi6rDqMVjJQlcatSvO_1SdemE.m4z6_KmV_JPWzx3GFVoB9p-ytleQL5vLVuNZItQ2dik

同理，再创建另外一个文件：
1. 文件路径为：http://www.kangyonggan.com/.well-known/acme-challenge/wpyYcbGItz-rmHKNFPrjrSnVy3vQ4cFC3fuRGvYF1l8
2. 文件内容为：wpyYcbGItz-rmHKNFPrjrSnVy3vQ4cFC3fuRGvYF1l8.m4z6_KmV_JPWzx3GFVoB9p-ytleQL5vLVuNZItQ2dik

需要注意的是：这两个文件的地址，一个有www，一个没有www，如果你前面没有包含www前缀的域名，你就只需要提供一个文件只可。

下面是我提供的文件：

![https](/upload/article/https08.png)

![https](/upload/article/https09.png)

把项目发布之后访问以下上面的两个地址，看看能不能访问通, 如果访问的通，点击【NEXT】

![https](/upload/article/https10.png)

如果校验没过，别放弃，多点几次，我就是在点了2次才通过的：

![https](/upload/article/https11.png)

在这一步一定一定不能忘了下载这两个文件，因为后面配置服务器的时候回用到这两个文件，最后点击【DONE NEXT】结束。

但是这个证书有效期是90天，90天之后需要再次申请，过程和这次一样。

## 使用Nginx配置SSL
把下载的两个文件重命名为domain.crt和domain.key,然后配置nginx：

```
server {
	server_name kangyonggan.com;
    listen 443;
    ssl on;
    ssl_certificate /root/ssl/domain.crt;
    ssl_certificate_key /root/ssl/domain.key;

	location / {
		proxy_pass http://localhost:8080;
	}

	# 设定访问静态文件直接读取不经过tomcat
	location ^~ /static/ {
			proxy_pass http://localhost:8080;
			root /WEB-INF/static/;
	}
}
```

重新加载nginx的配置：

```
nginx -s reload
```

访问https://kangyonggan.com和https://www.kangyonggan.com

![https](/upload/article/https12.png)

![https](/upload/article/https13.png)

虽然可以访问https了，但是原本的http仍然可以访问，所以还需要再配置。

## http重定向到https
把原本80端口的server配置：

```
server {
        listen 80 default_server;
        listen [::]:80 default_server;

        root /var/www/html;

        server_name _;

        location / {
                proxy_pass http://localhost:8080;
        }
}
```

改为：

```
server {
        listen 80 default_server;
        listen [::]:80 default_server;

        root /var/www/html;

        server_name _;

        location / {
                # proxy_pass http://localhost:8080;
                return 301 https://kangyonggan.com$request_uri;
        }
}
```

再次访问http的网站，就会被重定向到https了。










