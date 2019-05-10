---
title: 让百度分享支持https的网站
date: 2018-08-06 13:45:08
categories: Web前端
tags:
- JS
---

### 百度分享
百度分享地址：[http://share.baidu.com](http://share.baidu.com)
由于我的网站的https的，而百度分享代码中是http的，所以不能按照百度分享提供的代码直接使用。
我的做法是将百度分享所使用的js、css、image全部下载下来，放在我自己的服务器。

### 下载
[baidu-share.zip](/upload/article/baidu-share.zip)

### 使用方法
下面的代码是使用百度分享自动生成的，首先是把zip文件解压到服务器根目录下，然后需要把域名改为自己域名的就ok了。
比如我是将“http://bdimg.share.baidu.com/static/api/js/share.js”改为“${ctx}/static/api/js/share.js”

<!-- more -->

```
<script> window._bd_share_config = {
    "common": {
        "bdSnsKey": {},
        "bdText": "",
        "bdMini": "2",
        "bdMiniList": false,
        "bdPic": "",
        "bdStyle": "0",
        "bdSize": "16"
    }, "slide": {"type": "slide", "bdImg": "1", "bdPos": "right", "bdTop": "100"}
};
with (document)0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];</script>
</script>
```

### 效果图
![效果图](/upload/article/https-share.png)


