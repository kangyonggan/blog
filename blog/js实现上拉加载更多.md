---
title: js实现上拉加载更多
date: 2019-01-08 14:31:07
categories: Web前端
tags:
---

```
/**
 * 获取滚动条的位置
 *
 * @returns {number}
 */
function getScrollTop() {
    var scrollTop = 0;
    if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop;
    } else if (document.body) {
        scrollTop = document.body.scrollTop;
    }
    return scrollTop;
}

/**
 * 获取当前可视范围的高度
 *
 * @returns {number}
 */
function getClientHeight() {
    var clientHeight = 0;
    if (document.body.clientHeight && document.documentElement.clientHeight) {
        clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
    } else {
        clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
    }
    return clientHeight;
}

/**
 * 获取文档完整的高度
 *
 * @returns {number}
 */
function getScrollHeight() {
    return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
}

/**
 * 是否还有更多内容
 */
var hasMore = true;

/**
 * 上拉加载更多事件
 */
window.onscroll = function () {
    if (hasMore && getScrollTop() + getClientHeight() === getScrollHeight()) {
        console.log("加载更多...");
    }
};
```

> 当没有更多内容时，把hasMore置为false，否则会一直去尝试加载更多
