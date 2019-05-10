---
title: 纯前端markdown编辑器及解析
date: 2019-01-08 14:37:21
categories: Web前端
tags:
---

## markdown编辑器
下面是使用freemarker定义的宏，是一个markdown编辑器。

<!-- more -->

```
<#--markdown编辑器-->
<#macro markdown name id="" value="">
<#if id=="">
    <#local id=func('uuid', 'id')/>
</#if>
<link rel="stylesheet" href="${ctx}/libs/editor.md/css/editormd.css"/>
<div id="${id}">
    <textarea style="display: none" name="${name}">${value}</textarea>
</div>
<script src="${ctx}/libs/editor.md/editormd.js"></script>
<script>
    $(function () {
        editormd("${id}", {
            height: 600,
            syncScrolling: "single",
            path: "${ctx}/libs/editor.md/lib/",
            emoji: true,
            imageUpload: true,
            imageUploadURL: "${ctx}/file/markdown"
        });
    })
</script>
</div>
</#macro>
```

用例：<@ca.markdown name="content" value="${article.content!''}"/>

1. editor.md官网地址：[http://pandao.github.io/editor.md/examples/](http://pandao.github.io/editor.md/examples/)
2. <#local id=func('uuid', 'id')/> 是产生一个uuid。
3. imageUploadURL 是图片上传地址，需要写一个controller，如下。

```
/**
 * md编辑器文件、图片上传
 *
 * @param request 请求
 * @return 响应
 */
@PostMapping("markdown")
@ResponseBody
public Response markdown(HttpServletRequest request) {
    Response response = Response.getResponse();
    ServletContext context = request.getServletContext();

    //上传本地文件
    int success = 0;
    String message = "上传成功";
    try {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator iterator = multiRequest.getFileNames();
        if (iterator.hasNext()) {
            MultipartFile file = multiRequest.getFile(iterator.next().toString());
            String fileName = ftpService.genFileName("ARTICLE");
            // 上传到本地
            FileUpload.upload(fileUploadPath, fileName, file);

            fileName += "." + FilenameUtils.getExtension(file.getOriginalFilename());
            response.put("url", context.getContextPath() + "/upload/" + fileName);
            success = 1;
        }
    } catch (Exception e) {
        log.error("编辑器上传文件失败", e);
        message = "网络异常，请稍后再试!";
    }

    response.put("success", success);
    response.put("message", message);
    return response;
}
```

也就是要返回一个Map, 并带有success和message参数。

## 解析markdown内容
```

<#--markdown2html-->
<#macro md2html content>
    <#local uuid=func('uuid', 'id')/>
<link rel="stylesheet" href="${ctx}/libs/editor.md/css/editormd.preview.css"/>
<div id="${uuid}" class="markdown-content">
    <textarea style="display: none">${content}</textarea>
</div>

<script src="${ctx}/libs/editor.md//lib/marked.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/prettify.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/raphael.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/underscore.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/sequence-diagram.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/flowchart.min.js"></script>
<script src="${ctx}/libs/editor.md//lib/jquery.flowchart.min.js"></script>
<script src="${ctx}/libs/editor.md/editormd.js"></script>
<script type="text/javascript">
    $(function () {
        editormd.markdownToHTML("${uuid}", {
            path: "${ctx}/libs/editor.md/lib/",
            htmlDecode: "style,script,iframe",  // you can filter tags decode
            emoji: true,
            taskList: true,
            tex: true,  // 默认不解析
            flowChart: true,  // 默认不解析
            sequenceDiagram: true  // 默认不解析
        });
    });
</script>
</div>
</#macro>
```

用例：<@ca.md2html content="${article.content}"/>