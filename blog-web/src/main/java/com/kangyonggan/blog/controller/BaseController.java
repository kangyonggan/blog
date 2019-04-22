package com.kangyonggan.blog.controller;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.constants.Resp;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.interceptor.ParamsInterceptor;
import com.kangyonggan.blog.model.User;
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
     * 获取String类型的请求参数
     *
     * @param name 参数名
     * @return 返回参数值
     */
    protected String getStringParam(String name) {
        return ParamsInterceptor.getParameter(name);
    }

    /**
     * 获取String类型的请求参数, 带默认值
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return 返回参数值
     */
    protected String getStringParam(String name, String defaultValue) {
        return ParamsInterceptor.getParameter(name, defaultValue);
    }

    /**
     * 获取int类型的请求参数
     *
     * @param name 参数名
     * @return 返回int型的参数值
     */
    protected int getIntegerParam(String name) {
        return Integer.parseInt(ParamsInterceptor.getParameter(name));
    }

    /**
     * 获取int类型的请求参数, 带默认值
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return 返回int型的参数值
     */
    protected int getIntegerParam(String name, int defaultValue) {
        try {
            return Integer.parseInt(ParamsInterceptor.getParameter(name));
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
