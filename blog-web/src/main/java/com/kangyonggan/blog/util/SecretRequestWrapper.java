package com.kangyonggan.blog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kangyonggan.blog.constants.AppConstants;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author kangyonggan
 * @since 2019-04-15
 */
@Log4j2
public class SecretRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    private String aesKey;

    private String aesIv;

    private JSONObject params;

    public SecretRequestWrapper(HttpServletRequest request, String aesKey, String aesIv) throws IOException {
        super(request);
        this.aesKey = aesKey;
        this.aesIv = aesIv;
        this.params = new JSONObject();
        body = getBodyString(request).getBytes(Charset.forName(AppConstants.DEFAULT_CHARSET));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public JSONObject getAttrs() {
        if (params.isEmpty()) {
            try {
                String encryptedText = new String(body, AppConstants.DEFAULT_CHARSET);
                params = JSON.parseObject(Aes.desEncrypt(encryptedText, aesKey, aesIv));
            } catch (Exception e) {
                throw new RuntimeException("无法获取body中加密参数对应的json对象", e);
            }
        }

        return params;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    public static String getBodyString(ServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName(AppConstants.DEFAULT_CHARSET)));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
