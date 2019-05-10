---
title: js返回上一页的实现
date: 2019-01-08 14:29:10
categories: Web前端
tags:
---

如果有上一页则返回上一页，如果没上一页（直接输入url打开的网页）则返回首页。兼容主流浏览器。

```
/**
 * 返回
 */
function goBack() {
    if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
        // IE
        if (history.length > 0) {
            window.history.go(-1);
        } else {
            window.location.href = ctx + "/";
        }
    } else {
        // 非IE浏览器
        if (navigator.userAgent.indexOf('Firefox') >= 0 ||
            navigator.userAgent.indexOf('Opera') >= 0 ||
            navigator.userAgent.indexOf('Safari') >= 0 ||
            navigator.userAgent.indexOf('Chrome') >= 0 ||
            navigator.userAgent.indexOf('WebKit') >= 0) {

            if (window.history.length > 1) {
                window.history.go(-1);
            } else {
                window.location.href = ctx + "/";
            }
        } else {
            // 未知的浏览器
            window.history.go(-1);
        }
    }
}
```
