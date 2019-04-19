package com.kangyonggan.blog.configuration;

import com.kangyonggan.blog.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author kangyonggan
 * @since 2019/3/27 0027
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "fes:session")
public class HttpSessionConfigurer {

    /**
     * token放在http请求的header中，name=x-auth-token
     *
     * @return
     */
    @Bean
    public HeaderHttpSessionIdResolver httpSessionStrategy() {
        return new HeaderHttpSessionIdResolver(AppConstants.HEADER_TOKEN_NAME);
    }

}
