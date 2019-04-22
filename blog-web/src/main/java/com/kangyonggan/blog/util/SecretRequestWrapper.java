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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author kangyonggan
 * @since 2019-04-15
 */
@Log4j2
public class SecretRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 最终参数=原始参数+自定义参数
     */
    private Map<String, String[]> parameterMap = new HashMap<>();

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

        // 把原始参数放入最终参数中
        this.parameterMap.putAll(request.getParameterMap());

        // 把body中的json参数放入最终参数中
        JSONObject jsonObject = getAttrs();
        for (String key : jsonObject.keySet()) {
            addParameter(key, jsonObject.get(key));
        }
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

        if (params == null) {
            params = new JSONObject();
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

    /**
     * 把自定义参数放入最终参数中
     *
     * @param key
     * @param value
     */
    private void addParameter(String key, Object value) {
        if (value instanceof String[]) {
            parameterMap.put(key, (String[]) value);
        } else if (value instanceof String) {
            parameterMap.put(key, new String[]{(String) value});
        } else {
            parameterMap.put(key, new String[]{String.valueOf(value)});
        }
    }

    /**
     * 重写getParameter相关方法
     *
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String[] arr = parameterMap.get(name);
        if (arr != null && arr.length > 0) {
            return arr[0];
        }
        return null;
    }

    /**
     * 重写getParameter相关方法
     *
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    /**
     * 重写getParameter相关方法
     *
     * @return
     */
    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<>(parameterMap.keySet());
        return vector.elements();
    }

    /**
     * 重写getParameter相关方法
     *
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }
}

