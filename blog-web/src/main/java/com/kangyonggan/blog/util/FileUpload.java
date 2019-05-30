package com.kangyonggan.blog.util;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具类
 *
 * @author kangyonggan
 * @since 5/4/18
 */
public final class FileUpload {

    public static final String PICTURE_EXT = "jpeg,jpg,png,gif";

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private FileUpload() {

    }

    /**
     * 上传文件
     *
     * @param dir      文件父目录
     * @param fileName 文件名
     * @param file     文件
     * @throws FileUploadException 可能会抛出的异常
     */
    public static void upload(String dir, String fileName, MultipartFile file) throws FileUploadException {
        if (file.getSize() != 0) {
            try {
                File desc = getAbsolutePath(dir + fileName + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
                file.transferTo(desc);
            } catch (Exception e) {
                throw new FileUploadException("File Upload Exception", e);
            }
        }
    }

    private static File getAbsolutePath(String filename) throws IOException {
        File desc = new File(filename);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }
}