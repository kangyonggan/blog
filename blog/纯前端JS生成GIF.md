---
title: 纯前端JS生成GIF
date: 2019-01-18 10:42:35
categories: Web前端
tags:
---

## 实例
[https://kangyonggan.com/tool/gif](https://kangyonggan.com/tool/gif)
![](/upload/article/gif-1.png)

<!-- more -->

## 说明
1. 选择文件后并不会上传至服务器，因此你可以很大很大的图片。
2. 不管你选择多大的图片，最终生成的gif都会很小。
3. gif被压缩后会失真，会有一定程度的模糊。

## 依赖
[gif.zip](/upload/article/gif.zip)

## 代码
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<input type="file" data-index="0"/>
<input type="file" data-index="1"/>
<input type="file" data-index="2"/>

<button id="btn">开始生成</button>

<canvas width="400" height="300" id="canvas" style="display:none"></canvas>
<img src="" id="result">

<script src="../jquery/jquery-1.8.3.min.js"></script>
<script src="gif.js"></script>
<script>
    $(function () {
        // 生成gif的图片源
        var imgList = [];

        /**
         * 选择图片
         */
        $("input[type=file]").change(function (e) {
            e.preventDefault();
            var pic = this.files[0];
            // TODO 校验是不是图片格式（自行实现）

            // TODO 图片预览（自行实现，可参考：https://blog.kangyonggan.com/2019/01/14/%E7%BE%8E%E5%8C%96%E6%96%87%E4%BB%B6%E9%80%89%E6%8B%A9%E6%A1%86/）

            // 把选中的图片加到图片源 imgList
            var file = new FileReader();
            file.readAsDataURL(pic);

            // 图片的下标
            var index = $(this).data("index");

            file.onload = function () {
                imgList[index] = this.result;
            };
            return false;
        });

        var gif;
        var canvas = document.getElementById('canvas');
        var ctx = canvas.getContext('2d');

        /**
         * 开始生成
         */
        $("#btn").click(function () {
            gif = new GIF({
                workers: imgList.length,
                quality: 30,
                workerScript: "gif.worker.js"
            });
            gif.on("finished", function (blob) {
                var file = new FileReader();
                file.readAsDataURL(blob);
                file.onload = function () {
                    document.getElementById("result").setAttribute("src", file.result);
                }
            });

            var imgObjList = [], count = 0;

            for (var i = 0; i < imgList.length; i++) {
                var tmpImg = new Image();
                tmpImg.src = imgList[i];
                imgObjList.push(tmpImg);
                tmpImg.onload = function () {
                    count++;
                    if (count === imgList.length) {
                        generateGif(imgObjList);
                    }
                }
            }
        });

        /**
         * 生成gif
         *
         * @param imgObjList
         */
        function generateGif(imgObjList) {
            for (var i = 0; i < imgObjList.length; i++) {
                ctx.save();
                ctx.drawImage(imgObjList[i], 0, 0, canvas.width, canvas.height);

                // 给图片配字幕
//                ctx.font = "14px 宋体";
//                ctx.fillStyle = "White";
//                ctx.textAlign = "center";
//                ctx.fillText(title, 200, 280);

                ctx.restore();
                gif.addFrame(canvas, {copy: true, delay: 1500});
                ctx.clearRect(0, 0, canvas.width, canvas.height)
            }
            gif.render();
        }

    })
</script>
</body>
</html>
```

## 效果
![](/upload/article/gif-2.png)
