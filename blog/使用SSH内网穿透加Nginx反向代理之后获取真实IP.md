---
title: 使用SSH内网穿透加Nginx反向代理之后获取真实IP
date: 2018-08-06 13:45:08
categories: 系统运维
tags:
- Linux
---

![nginx](/upload/article/nginx-ssh.png)

## 配置Nginx
```
location / {
        # First attempt to serve request as file, then
        # as directory, then fall back to displaying a 404.
        # try_files $uri $uri/ =404;
        proxy_pass http://localhost:8080;

        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Host $host;
}
```

<!-- more -->

重新加载Nginx配置:

```
nginx -s reload
```

## JAVA获取IP
```
String ip = request.getHeader("X-Real-IP");
```

