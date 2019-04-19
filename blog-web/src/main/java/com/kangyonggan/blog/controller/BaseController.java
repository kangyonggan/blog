package com.kangyonggan.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.constants.Resp;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.interceptor.ParamsInterceptor;
import com.kangyonggan.blog.model.User;
import com.kangyonggan.blog.util.SecretRequestWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author kangyonggan
 * @since 8/9/18
 */
@Log4j2
@Secret
public class BaseController {

    /**
     * 异常捕获
     *
     * @param e 异常
     * @return 返回统一异常响应
     */
    @ExceptionHandler
    @ResponseBody
    public Response handleException(Exception e) {
        Response response = new Response();
        if (e != null) {
            log.error("捕获到异常", e);
            return response.failure(e.getMessage());
        }

        return response.failure();
    }

    /**
     * 生成一个成功响应
     *
     * @return
     */
    protected Response successResponse() {
        Response response = new Response();
        response.setRespCo(Resp.SUCCESS.getRespCo());
        response.setRespMsg(Resp.SUCCESS.getRespMsg());
        return response;
    }

    /**
     * 获取当前登录的用户
     *
     * @return
     */
    protected User currentUser() {
        return ParamsInterceptor.getUser();
    }

    /**
     * 获取当前登录用户的ID
     *
     * @return
     */
    protected Long currentUserId() {
        User user = currentUser();
        return user == null ? null : user.getUserId();
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    protected Integer getIntegerAttr(String key) {
        return getIntegerAttr(key, null);
    }

    /**
     * 获取参数
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected Integer getIntegerAttr(String key, Integer defaultValue) {
        Integer value = getAttrs().getInteger(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    protected String getStringAttr(String key) {
        return getStringAttr(key, null);
    }

    /**
     * 获取参数
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getStringAttr(String key, String defaultValue) {
        String value = getAttrs().getString(key);
        return value == null ? defaultValue : value;
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    protected Object getAttr(String key) {
        return getAttr(key, null);
    }

    /**
     * 获取参数
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected Object getAttr(String key, Object defaultValue) {
        return getAttrs().getOrDefault(key, defaultValue);
    }

    /**
     * 获取参数
     *
     * @return
     */
    protected JSONObject getAttrs() {
        SecretRequestWrapper request = (SecretRequestWrapper) ParamsInterceptor.getRequest();
        return request.getAttrs();
    }

}
