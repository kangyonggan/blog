---
title: 使用swift3开发iOS应用
date: 2018-08-06 13:45:08
categories: 综合
tags:
- IOS
---

代码托管地址：[https://github.com/kangyonggan/SmartNovel.git](https://github.com/kangyonggan/SmartNovel.git)

首页原型图：
![](/upload/article/ARTICLE201803291a669a8feedf06228961747698713e19548eaf00.jpeg)

<!-- more -->

![](/upload/article/ARTICLE20180329e44cec1dec1d3b9b03c291ce1fe7e5004f0b66f8.png)
![](/upload/article/ARTICLE20180329fe041a6ecab12d3375957a83f4f7cf955bb72e27.png)
![](/upload/article/ARTICLE20180329240316dc285f2176d97afb30cc96dfe1bbc49a46.png)
![](/upload/article/ARTICLE201803294da883b80dd96dbbd0f62effe47ad3ff1d652453.png)
![](/upload/article/ARTICLE20180329a444d1365aa78e878d553cba2a064f2126c5f488.png)
![](/upload/article/ARTICLE2018032946ff8ea3b8b64a176e59d15b8c605c450e7bd12e.png)
![](/upload/article/ARTICLE20180329db0e5f6eeb1e6f18a01ee8e2ad05b2ba5ac58736.png)
![](/upload/article/ARTICLE20180329b558fdcfc194a3c4bfbba132020e0e4e762f00aa.png)
![](/upload/article/ARTICLE20180329272ef2255bba2c536eae793fff6776ad5b758c8b.png)
![](/upload/article/ARTICLE201803293633b88664f2b300081beabeb72197377512e863.png)
![](/upload/article/ARTICLE20180329cab0385d756409741e63944e63ebf2f4315fb954.png)
![](/upload/article/ARTICLE20180329001d8e279d65048c0dcbc5dd6cb80173c98ddd81.png)
![](/upload/article/ARTICLE20180329bb4f8ff8d7313393276a745c47fb134dcbaa0dff.png)
![](/upload/article/ARTICLE20180329720c525c115fab4e2a83ceaec0bcef810c4c3ae5.png)
![](/upload/article/ARTICLE201803291531cee2ea814b6e16ea4319812f723682f6f42f.png)
![](/upload/article/ARTICLE201803293b4dda43256e5e42251c423d0175c6e490970a81.png)
![](/upload/article/ARTICLE20180329f8a98ddfdfab9152e46ae533f0b37bf76ca90ba0.png)
![](/upload/article/ARTICLE20180329a234d9ad240c728d1deaf9645a033b61c2590b0c.png)
![](/upload/article/ARTICLE20180329f69306e0c0aaf8099b51957865cb651ac9dbd6b2.png)
![](/upload/article/ARTICLE2018032977e8f4b2e8894951d7171b3997f9c765a4508a08.png)
![](/upload/article/ARTICLE2018032968448b2686352eb32ed5ce1d37023a656d039a9f.png)
![](/upload/article/ARTICLE201803293615191241b47d08cac1e20889433a8ea8dd640a.png)
![](/upload/article/ARTICLE201803292b4a1236a524db0b07ba01355e14b471f89be2e0.png)
![](/upload/article/ARTICLE20180329625a5540139caadf1a34ae98ea07ff47e914be1b.png)
![](/upload/article/ARTICLE20180329455eb5d4607a519c1e559a9f58e2a09946b7f5dc.png)
![](/upload/article/ARTICLE20180329a0e6417ee221afec014596baab5d9abb3becb035.png)
![](/upload/article/ARTICLE201803295b969d00d4a19654bdac5490704d5aef60c0d639.png)
![](/upload/article/ARTICLE20180329bfa1cbc3d5ae828b64112fb3ab2011222dd15e48.png)
![](/upload/article/ARTICLE20180329d8c5422b4948329769f4c956b1149d682a032a3f.png)
![](/upload/article/ARTICLE201803292e38ed339ce58b8395b0631708dd55ba00832b7a.png)
![](/upload/article/ARTICLE201803291f2be214eb728974170b91dfdb60e9e23e2f8329.png)
![](/upload/article/ARTICLE20180329eabd66b56aa94ef477cd9e095b2710d2c361023f.png)
![](/upload/article/ARTICLE2018032962047bfe0ada1ae75e5817d9143edab79f2d79f1.png)
![](/upload/article/ARTICLE2018032970b74281949244157e139cf98bc051d453675c0d.png)
![](/upload/article/ARTICLE20180329951e83b2162ddd233c81ee7a280590c6bd23886d.png)
![](/upload/article/ARTICLE20180329a264b52cdc39fa15a7c8740e53cd57d14271111d.png)
![](/upload/article/ARTICLE20180329be685eac962e536a0f60ef05fe7bd4e16974f076.png)
![](/upload/article/ARTICLE2018032993f0b186852babe40c80dbc5e1d1e30a7317a2bd.png)
![](/upload/article/ARTICLE20180329bb1431c798cd6668a7172026e101849d62030a56.png)
![](/upload/article/ARTICLE2018032958f05b24a4c0a5079163d9a222311cb51986e3c3.png)
![](/upload/article/ARTICLE2018032987576544d53a1772c257435bafcb2cff4e0423e3.png)
![](/upload/article/ARTICLE20180329fd58a4a8dd403e75b8b37452370b66f9861ded8f.png)
![](/upload/article/ARTICLE201803293b32a8e4f1198cc7126c3a268ec2476d9999ca67.png)

依赖第三方框架，是在项目的根目录添加`Podfile`文件

![](/upload/article/ARTICLE20180329922ba7ac1d5625efc4a50899a040dfc449036cd2.png)

其中`Podfile`的内容为：

```
platform :ios, '10.3'
use_frameworks!
 
target 'SmartNovel' do
	pod 'Toast-Swift'
	pod 'Just'
end
```

文件添加后之后执行命令`pod install`，执行成功之后如下图：

![](/upload/article/ARTICLE201803296b30f1712b6902876962f31dfc5f2163809c6443.png)

第三方框架报错，是因为它使用的是oc的语法，我使用的是swift语法，可能有些不兼容，点击自动修复即可。

![](/upload/article/ARTICLE2018032903a774a0cf2db0f3f20582b5ea0bad6b3c23e318.png)

把报错的代码删了就好，貌似没发现有其他影响。

![](/upload/article/ARTICLE20180329322d16429004dcf360118a34a77788eecaa4c386.png)

还有一些其他的小操作，我也知道的不多，不过这已经够我开发一个业余的小应用了。
