package com.kangyonggan.blog.advice;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.util.Aes;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author kangyonggan
 * @since 2019-04-15
 */
@RestControllerAdvice
@Log4j2
public class RequestAdvice extends RequestBodyAdviceAdapter {

    @Value("${app.aes-key}")
    private String aesKey;

    @Value("${app.aes-iv}")
    private String aesIv;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (enable(parameter)) {
            return new SecretHttpInputMessage(inputMessage, aesKey, aesIv, AppConstants.DEFAULT_CHARSET);
        }

        return inputMessage;
    }

    /**
     * 判断是否启用加密
     *
     * @param parameter
     * @return
     */
    public static boolean enable(MethodParameter parameter) {
        boolean enable = false;

        // 父类（第三优先级）
        Secret superSecret = parameter.getContainingClass().getSuperclass().getAnnotation(Secret.class);
        if (superSecret != null) {
            enable = superSecret.enable();
        }

        // 当前类（第二优先级）
        Secret classSecret = parameter.getContainingClass().getAnnotation(Secret.class);
        if (classSecret != null) {
            enable = classSecret.enable();
        }

        // 当前方法（第一优先级）
        Secret methodSecret = parameter.getMethod().getAnnotation(Secret.class);
        if (methodSecret != null) {
            enable = methodSecret.enable();
        }

        return enable;
    }

    /**
     * @author kangyonggan
     * @since 2019-04-15
     */
    private class SecretHttpInputMessage implements HttpInputMessage {

        /**
         * 请求头
         */
        private HttpHeaders headers;

        /**
         * 请求体
         */
        private InputStream body;

        SecretHttpInputMessage(HttpInputMessage inputMessage, String key, String iv, String charset) throws IOException {
            this.headers = inputMessage.getHeaders();
            String content = IOUtils.toString(inputMessage.getBody(), charset);
            if (StringUtils.isEmpty(content)) {
                this.body = inputMessage.getBody();
                return;
            }

            try {
                String decryptBody = Aes.desEncrypt(content, key, iv);
                this.body = IOUtils.toInputStream(decryptBody, charset);
            } catch (Exception e) {
                throw new IOException("入参解密异常", e);
            }
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}