package com.kangyonggan.blog.filter;

import com.kangyonggan.blog.util.SecretRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kangyonggan
 * @since 2019-04-15
 */
@Configuration
public class SecretRequestFilter extends OncePerRequestFilter {

    @Value("${app.aes-key}")
    private String aesKey;

    @Value("${app.aes-iv}")
    private String aesIv;

    @Value("${app.aes.white-list}")
    private String aesWhiteList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        filterChain.doFilter(new SecretRequestWrapper(request, aesKey, aesIv, aesWhiteList), response);
    }

}
