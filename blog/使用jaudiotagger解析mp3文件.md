---
title: 使用jaudiotagger解析mp3文件
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

#### 依赖
```
<jaudiotagger.version>2.0.3</jaudiotagger.version>

...

<dependency>
    <groupId>org</groupId>
    <artifactId>jaudiotagger</artifactId>
    <version>${jaudiotagger.version}</version>
</dependency>
```

<!-- more -->

#### Mp3Util.java
```
package com.kangyonggan.app.future.common.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * mp3工具类
 *
 * @author kangyonggan
 * @since 9/4/17
 */
@Log4j2
public class Mp3Util {

    public static void main(String[] args) throws Exception {
        Map<String, Object> resultMap = parse("/Users/kyg/Music/网易云音乐/demo.mp3", "/Users/kyg/Desktop");
        log.info(resultMap);
    }

    /**
     * 解析mp3
     *
     * @param mp3Path  mp3路径
     * @param albumDir 解析出的专辑图片存放目录
     * @return
     */
    public static Map<String, Object> parse(String mp3Path, String albumDir) {
        Map<String, Object> resultMap = new HashMap();

        FileOutputStream out = null;
        try {
            MP3File mp3File = new MP3File(mp3Path);
            MP3AudioHeader header = mp3File.getMP3AudioHeader();
            AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();

            // 时长(秒)
            int duration = header.getTrackLength();
            // 文件大小
            long size = mp3File.getFile().length();
            // 歌曲名
            String name = getInfo(id3v2Tag, "TIT2");
            // 歌手
            String singer = getInfo(id3v2Tag, "TPE1");
            // 专辑
            String album = getInfo(id3v2Tag, "TALB");
            // 专辑图片
            AbstractID3v2Frame frame = (AbstractID3v2Frame) id3v2Tag.frameMap.get("APIC");
            String mimeTpe = (String) frame.getBody().getObjectValue("MIMEType");
            byte data[] = (byte[]) frame.getBody().getObjectValue("PictureData");

            // 根据MIMEType获取对应的后缀，如：image/jpeg -> .jpg
            String extension = FileUtil.getExtension(mimeTpe);
            if (StringUtils.isEmpty(extension)) {
                extension = ".jpg";
            }
            // 把专辑图片写入指定文件夹
            out = new FileOutputStream(albumDir + File.separator + album + extension);
            out.write(data);
            out.flush();

            // 写响应
            resultMap.put("duration", duration);
            resultMap.put("size", size);
            resultMap.put("name", name);
            resultMap.put("singer", singer);
            resultMap.put("album", album);
            resultMap.put("mimeTpe", mimeTpe);
            resultMap.put("albumName", album + extension);

            resultMap.put("respCo", 0);
            resultMap.put("respMsg", "解析mp3成功");
        } catch (Exception e) {
            log.warn("解析mp3信息异常", e);
            resultMap.put("respCo", -1);
            resultMap.put("respMsg", e.getLocalizedMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.warn("关闭流异常", e);
                }
            }
        }

        return resultMap;
    }

    /**
     * 获取歌曲信息
     *
     * @param id3v2Tag
     * @param identifier
     * @return
     */
    private static String getInfo(AbstractID3v2Tag id3v2Tag, String identifier) {
        AbstractID3v2Frame frame = (AbstractID3v2Frame) id3v2Tag.frameMap.get(identifier);
        return (String) frame.getBody().getObjectValue("Text");
    }

}
```

其中依赖了apache下的一个jar包，用于获取MIMEType对应的文件后缀名

```
<tika.version>1.16</tika.version>

...

<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>${tika.version}</version>
</dependency>
```

#### FileUtil.java
```
/**
 * 根据MIMEType获取后缀
 *
 * @param mimeType
 * @return
 * @throws Exception
 */
public static String getExtension(String mimeType) throws Exception {
    MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
    MimeType type = allTypes.forName(mimeType);
    return type.getExtension();
}
```