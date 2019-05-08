package com.kangyonggan.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author kangyonggan
 * @since 2019-05-08
 */
@Controller
@RequestMapping("swagger")
public class SwaggerController {

    /**
     * 接口文档界面
     *
     * @return
     */
    @GetMapping
    public String index() {
        return "swagger.html";
    }

}
