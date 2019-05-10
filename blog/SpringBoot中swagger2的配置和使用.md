---
title: SpringBoot中swagger2的配置和使用
date: 2019-03-27 17:41:34
categories: Java后台
tags:
- Java
---

## 依赖
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.3.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

...


<!--swagger2-->
<swagger.version>2.9.2</swagger.version>

...

<!-- swagger -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>${swagger.version}</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>${swagger.version}</version>
</dependency>
```

<!-- more -->

## application.yml
```
spring:
  profiles:
    active: @env@
    
app:
  version: @project.version@
```

## SwaggerConfigurer.java
```
package com.kangyonggan.demo.configuration;

import com.kangyonggan.demo.constants.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kangyonggan
 * @since 2019-03-27
 */
@Configuration
@EnableSwagger2
public class SwaggerConfigurer {

    @Value("${app.version}")
    private String version;

    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("资源不存在").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());

        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        token.name(AppConstants.HEADER_TOKEN_NAME).description("令牌").modelRef(new ModelRef("string")).parameterType("header").build();
        parameters.add(token.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(!"prod".equals(env))
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台接口文档")
                .description("以下接口为前端开发者提供，如有疑问，请联系负责人，如下：")
                .contact(new Contact("康永敢", "https://kangyonggan.com", "java@kangyonggan.com"))
                .version(version)
                .build();
    }

}
```

## 使用
`LoginController.java`:

```
package com.kangyonggan.demo.controller;

import com.kangyonggan.demo.constants.AppConstants;
import com.kangyonggan.demo.dto.Response;
import com.kangyonggan.demo.interceptor.ParamsInterceptor;
import com.kangyonggan.demo.model.User;
import com.kangyonggan.demo.service.UserService;
import com.kangyonggan.demo.util.Digests;
import com.kangyonggan.demo.util.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 处理登录、登出相关请求
 *
 * @author kangyonggan
 * @since 2019-03-26
 */
@RestController
@Log4j2
@Api(tags = "LoginController", description = "登录相关接口")
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    @PostMapping("login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "电子邮箱", example = "admin@kangyonggan.com"),
            @ApiImplicitParam(name = "password", value = "密码", example = "11111111")
    })
    public Response login(@RequestParam String email, @RequestParam String password) {
        Response response = successResponse();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return response.failure("电子邮箱不存在");
        }
        if (user.getIsDeleted() == 1) {
            return response.failure("电子邮箱已被锁定");
        }

        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, AppConstants.HASH_INTERATIONS);
        String targetPassword = Encodes.encodeHex(hashPassword);
        if (!user.getPassword().equals(targetPassword)) {
            log.error("密码错误, ip:{}, email:{}", getIpAddress(), email);
            return response.failure("密码错误");
        }

        // 把登录信息放入session
        HttpSession session = ParamsInterceptor.getSession();
        session.setAttribute(AppConstants.KEY_SESSION_USER, user);
        log.info("登录成功,sessionId:{}", session.getId());
        return response;
    }

    /**
     * 注销
     *
     * @return
     */
    @GetMapping("logout")
    @ApiOperation("登出")
    public Response logout() {
        HttpSession session = ParamsInterceptor.getSession();
        log.info("登出:{}", session.getAttribute(AppConstants.KEY_SESSION_USER));
        session.removeAttribute(AppConstants.KEY_SESSION_USER);
        return successResponse();
    }

}
```

其中`Response.java`:

```
package com.kangyonggan.demo.dto;

import com.kangyonggan.demo.constants.Resp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用响应
 *
 * @author kangyonggan
 * @since 5/4/18
 */
@ApiModel(description = "通用响应")
@Data
public class Response implements Serializable {

    /**
     * 响应码
     */
    @ApiModelProperty(value = "响应码", required = true, example = "0000")
    private String respCo;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", required = true, example = "操作成功")
    private String respMsg;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据", required = true)
    private Map<String, Object> data = new HashMap<>(16);

    /**
     * 置为失败
     *
     * @return
     */
    public Response failure() {
        return failure(Resp.FAILURE.getRespCo(), Resp.FAILURE.getRespMsg());
    }

    /**
     * 置为失败
     *
     * @param respMsg
     * @return
     */
    public Response failure(String respMsg) {
        return failure(Resp.FAILURE.getRespCo(), respMsg);
    }

    /**
     * 置为失败
     *
     * @param respCo
     * @param respMsg
     * @return
     */
    public Response failure(String respCo, String respMsg) {
        this.respCo = respCo;
        this.respMsg = respMsg;
        this.data.clear();
        return this;
    }

    /**
     * put
     *
     * @param key
     * @param value
     * @return
     */
    public Response put(String key, Object value) {
        if (data == null) {
            data = new HashMap<>(16);
        }

        data.put(key, value);
        return this;
    }
}
```

## 效果图
![swagger](/upload/article/swagger.png)

