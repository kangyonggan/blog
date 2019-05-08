package com.kangyonggan.blog.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Html解析工具类
 *
 * @author kangyonggan
 * @since 5/4/18
 */
public final class HtmlUtil {

    /**
     * 默认失败重试次数
     */
    private static final int RETRY_COUNT = 10;

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private HtmlUtil() {
    }

    /**
     * 解析url
     *
     * @param url 页面地址
     * @return 返回页面文档
     */
    public static Document parseUrl(String url) {
        return parseUrl(url, RETRY_COUNT);
    }

    /**
     * 解析url
     *
     * @param url        页面地址
     * @param retryCount 重试次数
     * @return 返回页面文档
     */
    public static Document parseUrl(String url, int retryCount) {
        int cnt = 0;
        while (cnt < retryCount) {
            try {
                return Jsoup.connect(url).get();
            } catch (Exception e) {
                cnt++;
                try {
                    Thread.sleep(cnt * 500);
                } catch (InterruptedException e1) {

                }
            }
        }

        return null;
    }

}

