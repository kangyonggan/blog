package com.kangyonggan.blog.util;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author kangyonggan
 * @since 1/4/19
 */
@Component
public class FileHelper {

    /**
     * 文件跟路径
     */
    @Value("${app.file-upload}")
    @Getter
    private String fileUploadPath;

    /**
     * 获取文件名
     *
     * @param prefix
     * @return
     */
    public String genFileName(String prefix) {
        if (StringUtils.isNotEmpty(prefix)) {
            prefix += "_";
        } else {
            prefix = "";
        }

        String nextVal = String.valueOf(new Random().nextInt());
        String currentDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        return prefix + currentDate + StringUtils.leftPad(nextVal, 8, "0");
    }

}
