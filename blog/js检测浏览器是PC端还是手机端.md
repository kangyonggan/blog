---
title: js检测浏览器是PC端还是手机端
date: 2019-01-08 14:23:14
categories: Web前端
tags:
---

```
function IsPC() {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone",
        "SymbianOS", "Windows Phone",
        "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}

//true为PC端，false为手机端
var flag = IsPC();
if (!flag) {
    window.location.href = "${ctx}/wap"
}
```
