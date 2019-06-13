package com.kangyonggan.blog;

import com.kangyonggan.blog.service.sites.ArticleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kangyonggan
 * @since 2019-06-13
 */
public class RssTest extends AbstractTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void testGenRss() throws Exception {
        articleService.genRss();
    }

}
