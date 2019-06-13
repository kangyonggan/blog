package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.annotation.Secret;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.service.sites.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
@RestController
@RequestMapping("terminal")
@Secret(enable = false)
public class TerminalController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 生成rss
     *
     * @return
     * @throws Exception
     */
    @GetMapping
    public Response genRss() throws Exception {
        articleService.genRss();
        return successResponse();
    }

}
