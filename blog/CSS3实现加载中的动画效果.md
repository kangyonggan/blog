---
title: CSS3实现加载中的动画效果
date: 2018-08-06 13:45:08
categories: Web前端
tags:
- CSS
---

### 实现原理
主要使用transform的rotate属性，将线条组合成Loading图形 (也就是菊花图形)。

animation实现将线条颜色由浅到深，再由深到浅来回变换的动画，通过animation-delay属性来使颜色的变换产生过渡的效果，从而达到类似于Loading动画的效果。

### html代码
```
<div class="loading">
    <span class="line1"></span>
    <span class="line2"></span>
    <span class="line3"></span>
    <span class="line4"></span>
    <span class="line5"></span>
    <span class="line6"></span>
    <span class="line7"></span>
    <span class="line8"></span>
</div>
```

<!-- more -->

### css代码
```
.loading {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    width: 80px;
    height: 80px;
    margin: auto;
    z-index: 9999;
}

.loading span {
    width: 4px;
    height: 20px;
    background-color: #ccc;
    position: absolute;
    left: 38px;
    -webkit-animation: loading 1s infinite;
}

.loading .line1 {
    background-color: #000;
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
    -webkit-animation-delay: .3s;
}

.loading .line2 {
    top: 5px;
    left: 52px;
    -webkit-transform: rotate(45deg);
    transform: rotate(45deg);
    -webkit-animation-delay: .4s;
}

.loading .line3 {
    top: 18px;
    left: 57px;
    -webkit-transform: rotate(90deg);
    transform: rotate(90deg);
    -webkit-animation-delay: .5s;
}

.loading .line4 {
    top: 31px;
    left: 52px;
    -webkit-transform: rotate(135deg);
    transform: rotate(135deg);
    -webkit-animation-delay: .6s;
}

.loading .line5 {
    top: 37px;
    -webkit-animation-delay: .7s;
}

.loading .line6 {
    top: 32px;
    left: 24px;
    -webkit-transform: rotate(-135deg);
    transform: rotate(-135deg);
    -webkit-animation-delay: .8s;
}

.loading .line7 {
    top: 18px;
    left: 19px;
    -webkit-transform: rotate(-90deg);
    transform: rotate(-90deg);
    -webkit-animation-delay: .9s;
}

.loading .line8 {
    top: 5px;
    left: 24px;
    -webkit-transform: rotate(-45deg);
    transform: rotate(-45deg);
    -webkit-animation-delay: 1s;
}

@-webkit-keyframes loading {
    0% {
        background-color: #ccc;
    }
    50% {
        background-color: #000;
    }
    100% {
        background-color: #ccc;
    }
}
```

### 效果图

![loading-demo.gif](/upload/article/loading-demo.gif)
