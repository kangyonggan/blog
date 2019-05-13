package com.kangyonggan.blog.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http工具类
 *
 * @author kangyonggan
 * @since 5/4/18
 */
public final class HttpUtil {

    /**
     * 换行符
     */
    private static final String STR_LINE = "\n";

    /**
     * 私有构造, 任何时候都不能实例化
     */
    private HttpUtil() {

    }

    /**
     * 发送GET请求
     *
     * @param url 发送请求的URL
     * @return URL 所代表远程资源的响应结果
     * @throws Exception 可能发生的异常
     */
    public static String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * 发送GET请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数
     * @return URL 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String get(String url, String params) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            if (params != null) {
                url += "?" + params;
            }
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append(STR_LINE);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return result.toString();
    }

    /**
     * 发送POST请求
     *
     * @param url    请求的URL
     * @param params 请求参数
     * @return 返回响应结果
     * @throws Exception 请求发送异常
     */
    public static String post(String url, String params) throws Exception {
        StringBuilder result = new StringBuilder();
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append(STR_LINE);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return result.toString();
    }
}

