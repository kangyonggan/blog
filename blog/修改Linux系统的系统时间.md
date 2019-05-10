---
title: 修改Linux系统的系统时间
date: 2018-08-06 13:45:08
categories: 系统运维
tags:
- Linux
---

## 使用date命令
```
sudo date -s "2017-04-20 09:27:07"
```

改了之后再使用date命令查询时间，发现成功了，但是，大概几分钟之后又恢复成之前的错误时间了。

## 使用hwclock命令
```
sudo hwclock --set --date="04/20/17 09:24"
```

还是同样的问题，几分钟后就恢复了。

<!-- more -->

## 改时区

```
sudo cp /usr/share/zoneinfo/Asia/Shanghai ./localtime
```

亲测这个是ok的，即使重启也不会恢复成错的时间。