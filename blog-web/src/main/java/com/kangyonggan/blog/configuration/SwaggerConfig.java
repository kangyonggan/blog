package com.kangyonggan.blog.configuration;

import com.kangyonggan.blog.constants.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
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
 * @since 2019-04-12
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${app.version}")
    private String version;

    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();

        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        token.name(AppConstants.HEADER_TOKEN_NAME).description("令牌（需要登录权限的必填）").modelRef(new ModelRef("string")).parameterType("header").build();
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
        return new ApiInfoBuilder().title("东方骄子")
                .description("东方骄子为康永敢的个人博客网站")
                .termsOfServiceUrl("https://github.com/kangyonggan/blog")
                .contact(new Contact("康永敢", "https://kangyonggan.com", "java@kangyonggan.com"))
                .version(version)
                .build();

    }

}
