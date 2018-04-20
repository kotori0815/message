package com.msg.base.Swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wd on 2018/4/10.
 */
@Configuration //必须存在
@EnableSwagger2 //必须存在
@EnableWebMvc //必须存在
@ComponentScan(basePackages = {"com.msg.controller"})
public class MsgSwaggerConfig extends WebMvcConfigurerAdapter{
    /**
     *
     * 配置参数
     *
     * @return
     */
    @Bean
    public Docket buildDocket() {
        // 自定义参数
        List<Parameter> aParameters = new ArrayList<Parameter>();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(buildApiInf()).globalOperationParameters(aParameters)
                .select().apis(RequestHandlerSelectors.basePackage("com.msg.controller"))// controller路径
                .paths(PathSelectors.any()).build();
    }

    /**
     *
     * 文档信息
     *
     * @return
     */
    private ApiInfo buildApiInf() {
        return new ApiInfoBuilder().title("营销短信平台API").termsOfServiceUrl("").description("营销短信平台接口")
                .contact(new Contact("营销短信平台", "", "wd1995@126.com")).build();

    }
}
