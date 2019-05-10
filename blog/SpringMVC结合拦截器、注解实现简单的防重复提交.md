---
title: SpringMVC结合拦截器、注解实现简单的防重复提交
date: 2018-08-06 13:45:08
categories: Java后台
tags:
- Java
---

## 思路
1. 在请求一个表单页面时，服务端生成一个随机的token，把token放入session中并回传到前端页面。
2. 前端表单把token作为一个隐藏域提交给服务端。
3. 服务端校验提交的token和session中的token是否一致来判断是否是重复提交，然后清除session中的token。

## 想法
1. 服务端：在请求一个表单页面的controller的方法上加上注解@Token，即可生成token，放入session并回传到前端。
2. 客户端：我使用的是freemarker，想使用宏定义来封装一下表单组件，如果这个表单需要做防重复提交，只需要传入token=true即可，它会自动加入一个隐藏域，其值为服务端回传的token。
3. 服务端：如果表单提交的controller的方法上有注解@Token(type = Token.Type.CHECK),那么就校验session中的token和提交的token是否一致，从而判断是否是重复提交。

<!-- more -->

## 实现
### 自定义运行时注解@Token
```
package com.kangyonggan.cms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kangyonggan
 * @date 2018/5/1 0001
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Token {

    /**
     * TOKEN的key
     *
     * @return
     */
    String key();

    /**
     * TOKEN的操作类型
     *
     * @return
     */
    Type type() default Type.GENERATE;

    enum Type {
        /**
         * 生成TOKEN
         */
        GENERATE,
        /**
         * 校验TOKEN
         */
        CHECK
    }
}
```

### 自定义SpringMVC拦截器
在SpringMVC的配置文件`applicationContext-mvc.xml`中配置一个拦截器：

```
<!--MVC拦截器-->
<mvc:interceptors>
	<bean class="com.kangyonggan.cms.interceptor.HandlerInterceptor"></bean>
</mvc:interceptors>
```

`HandlerInterceptor`的实现如下：

```
package com.kangyonggan.cms.interceptor;

import com.kangyonggan.cms.annotation.Token;
import com.kangyonggan.cms.util.RandomUtil;
import com.kangyonggan.cms.util.ShiroUtils;
import com.kangyonggan.cms.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kangyonggan
 * @date 2018/4/21 0021
 */
@Log4j2
public class HandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Token token = method.getMethodAnnotation(Token.class);
            if (token != null && token.type() == Token.Type.CHECK) {
                if (isRepeatSubmit(request, token)) {
                    return false;
                }
            }
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Token token = method.getMethodAnnotation(Token.class);
            if (token != null && token.type() == Token.Type.GENERATE) {
                String random = RandomUtil.getRandomString();
                modelAndView.addObject("_token", random);
                request.getSession().setAttribute(token.key(), random);
                log.info("{}生成一个token，key={}, value={}", ShiroUtils.getShiroUsername(), token.key(), random);
            }
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 校验是否重复提交
     *
     * @param request
     * @param token
     * @return
     */
    private boolean isRepeatSubmit(HttpServletRequest request, Token token) {
        try {
            String random = request.getParameter("_token");
            String sessionRandom = (String) request.getSession().getAttribute(token.key());
            log.info("{}校验是否重复提交，key={}, random={}, sessionRandom={}", ShiroUtils.getShiroUsername(), token.key(), random, sessionRandom);
            if (StringUtil.hasEmpty(random, sessionRandom)) {
                return true;
            }
            return !random.equals(sessionRandom);
        } catch (Exception e) {
            log.error("校验是否重复提交异常", e);
            return true;
        } finally {
            request.getSession().removeAttribute(token.key());
        }
    }
}
```

## 使用
假设我现在需要修改用户信息，首先是请求一个用户修改页面，然后返回一个修改用户的表单页面，最后提交到服务端。

### 拦截请求页面的请求并生成token
`@Token(key = "editUser")`

```
/**
 * 编辑用户
 *
 * @param username
 * @param model
 * @return
 */
@RequestMapping(value = "{username:[\\w]+}/edit", method = RequestMethod.GET)
@RequiresPermissions("SYSTEM_USER")
@Token(key = "editUser")
public String edit(@PathVariable("username") String username, Model model) {
	model.addAttribute("user", userService.findUserByUsername(username));
	return getPathFormModal();
}
```

### 在表单中加入隐藏域
`token=true`

```
<@c.form id="modal-form" action="${ctx}/dashboard/system/user/${isEdit?string('update', 'save')}" token=true>
	<input type="hidden" id="old-username" value="${user.username!''}"/>
	<@c.input name="username" value="${user.username!''}" label="用户名" readonly=isEdit required=!isEdit valid={"isUsername": "true"}/>
	<@c.input name="realname" value="${user.realname!''}" label="真实姓名" required=true valid={"rangelength": "[1, 32]"}/>

	<#if !isEdit>
		<@c.input name="password" type="password" label="密码" required=true valid={"isPassword": "true"}/>
		<@c.input name="rePassword" type="password" label="确认密码" required=true valid={"equalTo": '#password'}/>
	</#if>
</@c.form>
```

`token=true`背后做了什么呢？其实就是在表单中加了一个隐藏域

```
<input type="hidden" name="_token" value="${_token!''}"/>
```

## 思考
我之所以给@Token注解添加一个key，是防止一种特殊的情况：

1. 请求表单A，但是未提交。
2. 请求表单B，也未提交。
3. 回到表单A，提交。

如果没有设计`key`, 那么所有表单的token放在session中的key都是一样的，后面请求的token就会覆盖前面的token，会导致前面的表单无法提交。




