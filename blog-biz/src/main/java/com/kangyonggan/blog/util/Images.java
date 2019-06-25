package com.kangyonggan.blog.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileMetadataDirectory;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @date 6/23/17
 */
public final class Images {

    private Images() {
    }

    /**
     * 生成缩略图。
     *
     * @param source 原图（大）
     * @param desc   目标图片
     * @param width  目标宽
     * @param height 目标高
     * @throws Exception
     */
    public static void thumb(String source, String desc, int width, int height) throws Exception {
        BufferedImage image = ImageIO.read(new FileInputStream(source));

        // 判断原图的长宽比是不是比目标图的长宽比大，用于计算压缩后的宽和高
        int pressWidth;
        int pressHeight;
        if (image.getHeight() * 1.0 / image.getWidth() > height * 1.0 / width) {
            // 原图是高图
            // 让压缩后的宽度等于目标宽度
            pressWidth = width;
            // 等比例计算高度
            pressHeight = (int) (1.0 * image.getHeight() * width / image.getWidth());
        } else {
            // 原图是宽图
            // 让压缩后的高度等于目标高度
            pressHeight = height;
            // 等比例计算高度
            pressWidth = (int) (1.0 * image.getWidth() * height / image.getHeight());
        }

        // 先压缩
        BufferedImage temp = zoomImage(image, pressWidth, pressHeight);

        // 再截取
        int x = (temp.getWidth() - width) / 2;
        int y = (temp.getHeight() - height) / 2;
        temp = temp.getSubimage(x, y, width, height);

        // 最后写文件
        ImageIO.write(temp, "png", new FileOutputStream(desc));
    }

    /**
     * 缩放图片
     *
     * @param image
     * @param w
     * @param h
     * @throws Exception
     */
    private static BufferedImage zoomImage(BufferedImage image, int w, int h) throws Exception {
        double wr = w * 1.0 / image.getWidth();
        double hr = h * 1.0 / image.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        return ato.filter(image, null);
    }

    /**
     * 解析图片
     *
     * @param imgPath
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseImg(String imgPath) throws Exception {
        Map<String, Object> map = new HashMap<>(32);
        File jpegFile = new File(imgPath);
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

        for (Directory directory : metadata.getDirectories()) {
            if (directory == null) {
                continue;
            }

            if (directory instanceof ExifSubIFDDirectory) {
                ExifSubIFDDirectory exifSubIFDDirectory = (ExifSubIFDDirectory) directory;
                String width = exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH);
                String height = exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT);
                // 图片宽度
                map.put("width", width);
                // 图片高度
                map.put("height", height);
            } else if (directory instanceof FileMetadataDirectory) {
                FileMetadataDirectory fileMetadataDirectory = (FileMetadataDirectory) directory;
                String name = fileMetadataDirectory.getString(FileMetadataDirectory.TAG_FILE_NAME);
                String size = fileMetadataDirectory.getString(FileMetadataDirectory.TAG_FILE_SIZE);
                Date date = fileMetadataDirectory.getDate(FileMetadataDirectory.TAG_FILE_MODIFIED_DATE);
                // 文件名称
                map.put("name", name);
                // 文件大小
                map.put("size", size);
                // 修改时间
                map.put("modifiedDate", date.getTime());
            } else if (directory instanceof ExifIFD0Directory) {
                ExifIFD0Directory exifIFD0Directory = (ExifIFD0Directory) directory;
                String make = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MAKE);
                String model = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
                String version = exifIFD0Directory.getString(ExifIFD0Directory.TAG_SOFTWARE);
                Date date = exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME);
                if (StringUtils.isNotEmpty(make)) {
                    // 设备
                    map.put("make", make);
                }
                if (StringUtils.isNotEmpty(model)) {
                    // 型号
                    map.put("model", model);
                }
                if (StringUtils.isNotEmpty(version)) {
                    // 版本
                    map.put("version", version);
                }
                if (date != null) {
                    // 拍摄时间
                    map.put("datetime", date.getTime());
                }
            } else if (directory instanceof GpsDirectory) {
                GpsDirectory gpsDirectory = (GpsDirectory) directory;
                Rational[] longitude = gpsDirectory.getRationalArray(GpsDirectory.TAG_LONGITUDE);
                Rational[] latitude = gpsDirectory.getRationalArray(GpsDirectory.TAG_LATITUDE);
                // 经度
                map.put("longitude", longitude[0].intValue() + "°" + longitude[1].intValue() + "'" + longitude[2].intValue() + "\"");
                // 维度
                map.put("latitude", latitude[0].intValue() + "°" + latitude[1].intValue() + "'" + latitude[2].intValue() + "\"");
            }
        }

        return map;
    }

}
