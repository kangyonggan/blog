package com.kangyonggan.blog.advice;

import com.alibaba.fastjson.JSON;
import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.util.Aes;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author kangyonggan
 * @since 2019-04-15
 */
@RestControllerAdvice
@Log4j2
public class ResponseAdvice implements ResponseBodyAdvice {

    @Value("${app.aes-key}")
    private String aesKey;

    @Value("${app.aes-iv}")
    private String aesIv;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (enable(methodParameter)) {
            try {
                return Aes.encrypt(JSON.toJSONString(o), aesKey, aesIv);
            } catch (Exception e) {
                throw new RuntimeException("出参加密异常", e);
            }
        }

        return o;
    }

    /**
     * 判断是否启用加密
     *
     * @param parameter
     * @return
     */
    private boolean enable(MethodParameter parameter) {
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
}
