package com.kangyonggan.blog.configuration;

import com.kangyonggan.blog.constants.AppConstants;
import com.kangyonggan.blog.interceptor.AuthInterceptor;
import com.kangyonggan.blog.interceptor.ParamsInterceptor;
import com.kangyonggan.blog.service.system.MenuService;
import com.kangyonggan.blog.service.system.RoleService;
import com.kangyonggan.blog.util.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author longjie
 * @since
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    /**
     * 文件跟路径
     */
    @Value("${app.file-upload}")
    private String fileUploadPath;

    @Value("${app.aes-key}")
    private String aesKey;

    @Value("${app.aes-iv}")
    private String aesIv;

    /**
     * 允许跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").exposedHeaders(AppConstants.HEADER_TOKEN_NAME);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RoleService roleService = SpringUtils.getBean(RoleService.class);
        MenuService menuService = SpringUtils.getBean(MenuService.class);

        // 处理请求
        registry.addInterceptor(new ParamsInterceptor()).addPathPatterns("/**");
        // 登录认证
        registry.addInterceptor(new AuthInterceptor(roleService, menuService, aesKey, aesIv)).addPathPatterns("/**");
    }

    /**
     * 处理上传文件的路径
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + fileUploadPath);
    }
}
