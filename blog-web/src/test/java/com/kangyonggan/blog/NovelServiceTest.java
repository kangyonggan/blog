package com.kangyonggan.blog;

import com.kangyonggan.blog.service.sites.NovelService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author kangyonggan
 * @since 7/19/18
 */
public class NovelServiceTest extends AbstractTest {

    @Autowired
    private NovelService novelService;

    @Test
    public void testPull() throws IOException {
        novelService.pullNovels("96");

        System.in.read();
    }

}
