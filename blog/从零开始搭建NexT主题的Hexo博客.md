---
title: 从零开始搭建NexT主题的Hexo博客
date: 2017-03-29 00:11:46
categories: 综合
tags:
- Linux
---


> 参考文档: [http://theme-next.iissnan.com/getting-started.html](http://theme-next.iissnan.com/getting-started.html)

# 系统版本
```
root@iZ23ldh8kudZ:~# cat /etc/issue
Ubuntu 16.04.2 LTS \n \l
```

其他系统搭建流程类似，这里不一一演示。

# 准备工作
关于hexo和next的基本概念这里不再介绍，只说怎么安装和使用。

<!-- more -->

### 安装Git
```
root@iZ23ldh8kudZ:~# apt-get update
root@iZ23ldh8kudZ:~# apt-get install git
```

### 安装Node.js
```
root@iZ23ldh8kudZ:~# apt-get install nodejs
root@iZ23ldh8kudZ:~# apt-get install npm
```

<!-- more -->

# 安装Hexo
```
root@iZ23ldh8kudZ:~# npm install -g hexo-cli
```

发现报错, 经谷歌后，在需要安装nodejs-legacy：

```
root@iZ23ldh8kudZ:~# apt-get install nodejs-legacy
```

再次安装hexo-cli后成功！

```
root@iZ23ldh8kudZ:~# hexo -version
hexo-cli: 1.0.2
os: Linux 4.4.0-63-generic linux x64
http_parser: 2.5.0
node: 4.2.6
v8: 4.5.103.35
uv: 1.8.0
zlib: 1.2.8
ares: 1.10.1-DEV
icu: 55.1
modules: 46
openssl: 1.0.2g-fips
```

# 建站
```
root@iZ23ldh8kudZ:~# hexo init blog
root@iZ23ldh8kudZ:~# cd blog/
root@iZ23ldh8kudZ:~/blog# npm install
```

### 启动
```
root@iZ23ldh8kudZ:~/blog# hexo s
```

### 查看
在浏览器中输入localhost:4000，查看效果如下：

![hexo-01](/upload/article/hexo-01.png)

至此，hexo博客就搭建好了，接下来就是安装NexT主题并且进行各种配置了。

# 安装NexT主题
```
root@iZ23ldh8kudZ:~/blog# pwd
/root/blog
root@iZ23ldh8kudZ:~/blog# git clone https://github.com/iissnan/hexo-theme-next themes/next
```

安装成功之后，会发现主题文件夹下面多了一个`next`文件夹

```
root@iZ23ldh8kudZ:~/blog# ll themes/
total 16
drwxr-xr-x 4 root root 4096 Mar 28 17:58 ./
drwxr-xr-x 6 root root 4096 Mar 28 17:48 ../
drwxr-xr-x 6 root root 4096 Mar 28 17:26 landscape/
drwxr-xr-x 9 root root 4096 Mar 28 17:59 next/
```

### 使用next主题
编辑`站点配置文件`, 修改theme配置的值：

```
theme: next
```

重启hexo，查看界面效果：

![hexo-02](/upload/article/hexo-02.png)

> 提示：修改站点配置需要重启，修改主题文件不需要重启，如果改了没生效，请运行`hexo clean`

个人感觉这个有点丑，所以我又换了一个风格，next提供了3中风格的主题：

- Muse - 默认 Scheme，这是 NexT 最初的版本，黑白主调，大量留白
- Mist - Muse 的紧凑版本，整洁有序的单栏外观
- Pisces - 双栏 Scheme，小家碧玉似的清新

修改`主题配置文件`, 修改scheme配置的值：

```
scheme: Mist
```

重启hexo，查看界面效果：

![hexo-03](/upload/article/hexo-03.png)

个人比较喜欢这种风格的主题。

# 个性化设置
### 网站相关设置
修改`站点配置文件`， Site相关配置默认如下：

```
# Site
title: Hexo
subtitle:
description:
author: John Doe
language:
timezone:
```

经过配置后：

```
# Site
title: 东方骄子 
subtitle:
description: 二逼青年欢乐多
author: 康永敢
language: zh-Hans
timezone:
```

设置`favicon`： 把favicon.ico放在`source/`目录下即可。

设置作者头像：  
修改`主题配置文件`：

```
# Sidebar Avatar
# in theme directory(source/images): /images/avatar.jpg
# in site  directory(source/uploads): /uploads/avatar.jpg
avatar: /uploads/avatar.png
```

然后把你的头像(avatar.png)放在`themes/next/source/uploads/`目录下，没有uploads目录的话可以自己创建一个。


重启后查看效果如下：

![hexo-04](/upload/article/hexo-04.png)

> 头像可以设置成gif动态图！

### 菜单相关设置
修改`主题配置文件`，默认菜单相关配置如下：

```
# ---------------------------------------------------------------
# Menu Settings
# ---------------------------------------------------------------

menu:
  home: /
  #categories: /categories
  #about: /about
  archives: /archives
  tags: /tags
  #sitemap: /sitemap.xml
  #commonweal: /404.html


# Enable/Disable menu icons.
# Icon Mapping:
#   Map a menu item to a specific FontAwesome icon name.
#   Key is the name of menu item and value is the name of FontAwesome icon. Key is case-senstive.
#   When an question mask icon presenting up means that the item has no mapping icon.
menu_icons:
  enable: true
  #KeyMapsToMenuItemKey: NameOfTheIconFromFontAwesome
  home: home
  about: user
  categories: th
  schedule: calendar
  tags: tags
  archives: archive
  sitemap: sitemap
  commonweal: heartbeat

```

本人配置后如下：

```
# ---------------------------------------------------------------
# Menu Settings
# ---------------------------------------------------------------

menu:
  home: /
  categories: /categories
  about: /about
  archives: /archives
  tags: /tags
  sitemap: /sitemap.xml
  commonweal: /404.html


# Enable/Disable menu icons.
# Icon Mapping:
#   Map a menu item to a specific FontAwesome icon name.
#   Key is the name of menu item and value is the name of FontAwesome icon. Key is case-senstive.
#   When an question mask icon presenting up means that the item has no mapping icon.
menu_icons:
  enable: true
  #KeyMapsToMenuItemKey: NameOfTheIconFromFontAwesome
  home: home
  about: user
  categories: th
  schedule: calendar
  tags: tags
  archives: archive
  sitemap: sitemap
  commonweal: heartbeat
```

图标我没换变，用的默认的，只是多显示了几个菜单而已，当然，现在的菜单是不能正常使用的，还需要再做一些操作。

效果如下:

![hexo-05](/upload/article/hexo-05.png)

现在看起来好像该有的都有了，但是点击之后会报错，比如点击“关于”菜单：

![hexo-06](/upload/article/hexo-06.png)

#### 关于

发现缺少“关于”页面，下面就来添加关于页面：

```
root@iZ23ldh8kudZ:~/blog# hexo new page about
INFO  Created: ~/blog/source/about/index.md
root@iZ23ldh8kudZ:~/blog# ll source/
total 36
drwxr-xr-x 4 root root  4096 Mar 28 21:18 ./
drwxr-xr-x 6 root root  4096 Mar 28 20:24 ../
drwxr-xr-x 2 root root  4096 Mar 28 21:18 about/
-rw-r--r-- 1 root root 16958 Mar 28 20:58 favicon.ico
drwxr-xr-x 2 root root  4096 Mar 28 17:26 _posts/
```

发现在source目录下了生成一个about文件夹，about里面是一个md文件， 内容为：

```
root@iZ23ldh8kudZ:~/blog# cat source/about/index.md 
---
title: about
date: 2017-03-28 21:18:40
---
```

本人编辑后内容为：

```
---
title: 关于作者
date: 2017-03-25 14:15:25
comments: false
---

### 基础信息
- 姓名：康永敢
- 性别：男
- 职业：Java开发

### 联系方式
- 手机：18221372104
- 邮箱：kangyonggan@gmail.com
- QQ：2825176081
- 现住址：上海市松江区九亭镇
- 工作地址：上海市南京西路399号明天广场21楼（华信证券）
```

其中`comments`表示此页面不需要评论，关于评论的问题下面会讨论。  
刷新后界面如下：

![hexo-07](/upload/article/hexo-07.png)

#### 404
404页面我用的是公益404，在source目录下创建404.html, 内容如下：

```
<!DOCTYPE HTML>
<html>
<head>
  <meta http-equiv="content-type" content="text/html;charset=utf-8;"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="robots" content="all" />
  <meta name="robots" content="index,follow"/>
  <link rel="stylesheet" type="text/css" href="https://qzone.qq.com/gy/404/style/404style.css">
</head>
<body>
  <script type="text/plain" src="http://www.qq.com/404/search_children.js"
          charset="utf-8" homePageUrl="/"
          homePageName="回到我的主页">
  </script>
  <script src="https://qzone.qq.com/gy/404/data.js" charset="utf-8"></script>
  <script src="https://qzone.qq.com/gy/404/page.js" charset="utf-8"></script>
</body>
</html>
```

刷新后效果如下：

![hexo-08](/upload/article/hexo-08.png)

#### 标签
```
root@iZ23ldh8kudZ:~/blog# hexo new page tags
INFO  Created: ~/blog/source/tags/index.md
root@iZ23ldh8kudZ:~/blog# cat source/tags/index.md 
---
title: tags
date: 2017-03-28 21:33:05
---
root@iZ23ldh8kudZ:~/blog# 
```

经过我的修改后内容为：

```
---
title: 全部标签
date: 2017-03-25 14:13:35
type: tags
comments: false
---
```

刷新后看效果：

![hexo-09](/upload/article/hexo-09.png)

刷新后看不到什么效果，因为你还没有“标签”，怎么才能有标签呢？不急，下面会说的。

#### 分类
```
root@iZ23ldh8kudZ:~/blog# hexo new page categories
INFO  Created: ~/blog/source/categories/index.md
root@iZ23ldh8kudZ:~/blog# cat source/categories/index.md 
---
title: categories
date: 2017-03-28 21:37:42
---
root@iZ23ldh8kudZ:~/blog# 
```

经过我的修改后内容为：

```
---
title: 全部分类
date: 2017-03-25 14:15:11
type: categories
comments: false
---
```

刷新后看效果：

![hexo-10](/upload/article/hexo-10.png)

刷新后看不到什么效果，原因同上。

# 文章
其他的配置现在不好说，因为没有文章！所以接下来我会先创建一些文章。

### 文章模板
在scaffolds目录下是创建新文章时的模板:

```
root@iZ23ldh8kudZ:~/blog# ll scaffolds/
total 20
drwxr-xr-x 2 root root 4096 Mar 28 17:26 ./
drwxr-xr-x 6 root root 4096 Mar 28 20:24 ../
-rw-r--r-- 1 root root   33 Mar 28 17:26 draft.md
-rw-r--r-- 1 root root   44 Mar 28 17:26 page.md
-rw-r--r-- 1 root root   50 Mar 28 17:26 post.md
```

默认使用的是post.md这个模板，你也可以在站点文件中配置其他模板：

```
default_layout: post
```

不过一般也不需要改，我是直接改的post.md，改后内容如下：

```
---
title: {{ title }}
date: {{ date }}
categories:
tags: 
---
```
	
使用模板创建一篇文章：
	
```
root@iZ23ldh8kudZ:~/blog# hexo new SSH免密登录
INFO  Created: ~/blog/source/_posts/SSH免密登录.md
root@iZ23ldh8kudZ:~/blog# 
```

`hexo new <title>`命令会使用默认模板创建一篇文章，文章在source/_post/文件夹下。
	
为了观察实际效果，我在这篇文章中添加一些真实的内容
	
刷新后效果如下：

![hexo-11](/upload/article/hexo-11.png)

这时候你再去查看“标签”页和“分类”页，应该就能看见有内容了。

### 分页
为了看出分页效果，我先把hexo例子中的Hello World干掉，另外把我的博客搬进_post中。  
修改`站点配置文件`中的`per_page`配置，默认是10，我改为5:

```
# Pagination
## Set per_page to 0 to disable pagination
per_page: 5
pagination_dir: page
```

效果如下：

![hexo-12](/upload/article/hexo-12.png)

# 其他
至此，博客就已经有型了，但是还得经典细琢。

### 分享
当我们看到一篇好文章时想分享给其他人看怎么办？复制url?太low了！

修改`主题配置文件`的`jiathis`:

```
# Share
jiathis: true
# Warning: JiaThis does not support https.
#add_this_id:
```

刷新文章详情页面,可以在底部看到分享按钮：

![hexo-13](/upload/article/hexo-13.png)

### 社交链接

修改`主题配置文件`的`Social`相关配置:

```
# Social Links
# Key is the link label showing to end users.
# Value is the target link (E.g. GitHub: https://github.com/iissnan)
social:
  Github: https://github.com/kangyonggan/
  Book: http://kangyonggan.com:6666/

# Social Links Icons
# Icon Mapping:
#   Map a menu item to a specific FontAwesome icon name.
#   Key is the name of the item and value is the name of FontAwesome icon. Key is case-senstive.
#   When an globe mask icon presenting up means that the item has no mapping icon.
social_icons:
  enable: true
  # Icon Mappings.
  # KeyMapsToSocialItemKey: NameOfTheIconFromFontAwesome
  GitHub: github
  Twitter: twitter
  Weibo: weibo
  Book: book
```

刷新后效果如下：

![hexo-14](/upload/article/hexo-14.png)


发现进入详情界面后，自动弹出右边目录结构（如果文章有目录的时候），设置`主题配置文件`，让进入详情界面的时候不要自动弹目录结构：  
修改sidebar:display的值： 

```
sidebar:
  # Sidebar Position, available value: left | right
  position: left
  #position: right

  # Sidebar Display, available value:
  #  - post    expand on posts automatically. Default.
  #  - always  expand for all pages automatically
  #  - hide    expand only when click on the sidebar toggle icon.
  #  - remove  Totally remove sidebar including sidebar toggle.
  #display: post
  #display: always
  display: hide
  #display: remove

  # Sidebar offset from top menubar in pixels.
  offset: 12
  offset_float: 0

  # Back to top in sidebar
  b2t: false

  # Scroll percent label in b2t button
  scrollpercent: false

```

### 生成RSS

```
root@iZ23ldh8kudZ:~/blog# npm install hexo-generator-feed --save
```

修改`主题配置文件`的rss配置，如下：

```
# Set rss to false to disable feed link.
# Leave rss as empty to use site's feed link.
# Set rss to specific value if you have burned your feed already.
rss: 

feed:
  type: atom
  path: atom.xml
  limit: 20
  hub:
  content:
```

rss配置没变，feed相关的为新增的, 刷新后就可以在sidebar中看见rss链接了。 

![hexo-15](/upload/article/hexo-15.png)
 
可以使用`rss阅读器`订阅这个rss地址的文章。

### 打赏
修改`主题配置文件`中的`alipay`的值(如果没有alipay就新增):

```
# pay
alipay: /uploads/ipay.png
```

然后把支付宝的收款二维码放到`themes/next/source/uploads/`目录下。

文章详情界面的效果如图：

![hexo-16](/upload/article/hexo-16.png)

### 代码高亮风格
修改`主题配置文件`的`highlight_theme`的值:

```
# Code Highlight theme
# Available value:
#    normal | night | night eighties | night blue | night bright
# https://github.com/chriskempson/tomorrow-theme
highlight_theme: night
```

### 站点地图
```
npm install hexo-generator-sitemap --save
npm install hexo-generator-baidu-sitemap --save
```

修改`站点配置文件`，在最后添加(可以不加，因为有缺省值)：

```
# Sitemap Setting
sitemap:
  path: sitemap.xml
baidusitemap:
  path: baidusitemap.xml
```

重启后访问`http://localhost:4000/sitemap.xml`，就可以看到内容了。 

![hexo-18](/upload/article/hexo-18.png)

但是连接地址是错的，所以我们需要在`站点配置文件`中修改：


```
# URL
## If your site is put in a subdirectory, set url as 'http://yoursite.com/child' and root as '/child/'
url: http://kangyonggan.com
root: /
permalink: :year/:month/:day/:title/
permalink_defaults:
```

下面是提交`站点地图`到百度站长工具中的过程, 不搞SEO的可以略过

![hexo-17](/upload/article/hexo-17.png)

### 百度统计
在[http://tongji.baidu.com/](http://tongji.baidu.com/)注册账号，并创建应用，然后在“代码获取”界面获取`baidu_analytics`，如下图：

![hexo-19](/upload/article/hexo-19.png)

修改`主题配置文件`的`baidu_analytics`的值：

```
# Baidu Analytics ID
baidu_analytics: 9a7a48ed52f9726****8a0955ae72adf
```

为了个人id不被盗用，我隐藏了四位。一段时间之后查看访问量：

![hexo-20](/upload/article/hexo-20.png)


### 评论
之前的hexo用户使用`多说`评论插件的比较多，但是现在多说即将关闭，新用户已经不能使用了，所有我使用`畅言`。

注册畅言[http://changyan.kuaizhan.com/](http://changyan.kuaizhan.com/)并创建站点，获取`APP ID`和`APP KEY`。

![hexo-21](/upload/article/hexo-21.png)

把这两个的值写入`主题配置文件`:

```
# changyan
changyan:
  enable: true
  appid: cy****H1C
  appkey: 5bc9ff33a197******b38cc87994bf4f
  count: true
```

效果如下：

![hexo-22](/upload/article/hexo-22.png)

我这里的评论框是黑色的，你可以在“畅言”网站上修改评论框的样式。

### 站内搜索
这是个很好用的功能，就是反应有点慢。修改`主题配置文件`的`local_search`的值：

```
# Local search
local_search:
  enable: true
```

修改这个配置可以在界面上看到“搜索”按钮。但点击无效。

修改`站点配置文件`，在最后新增：

```
# 搜索
search:
  path: search.xml
  field: post
  format: html
  limit: 10000
```

安装搜索插件：

```
npm install hexo-generator-search --save 
```

重启后搜索效果如下：

![hexo-23](/upload/article/hexo-23.png)

# hexo常用命令
### 清除
```
hexo clean
```

### 生成站点
```
hexo gengrate
```

可以简写为`hexo g`

### 本地启动
```
hexo server
```

可以简写为`hexo s`

还可以带参数，比如`hexo s --debug`会在命令窗口打印日志以供调试。

### 发布
```
hexo deploy
```

简写为`hexo d`

需要在`站点配置文件`中配置`deploy`相关参数：

```
# Deployment
## Docs: https://hexo.io/docs/deployment.html
deploy:
  type: git
  repo: https://github.com/kangyonggan/kangyonggan.github.io.git
```

此外还需要安装deploy相关插件，并且配置git全局用户相关变量，不然没权限推送到github上。

如果没云空间的可以在deploy到github上托管，但github最近比较慢，所以我是放在云服务器上的。
