package com.kangyonggan.blog.controller.web;

import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2019-04-24
 */
@RestController
@RequestMapping("article")
public class ArticleController extends BaseController {

    @PostMapping("detail")
    public Response detail(@RequestParam("articleId") Long articleId) {
        Response response = successResponse();
        Map<String, Object> article = new HashMap<>(4);
        article.put("content", "文章" + articleId);

        response.put("article", article);
        return response;
    }

}
