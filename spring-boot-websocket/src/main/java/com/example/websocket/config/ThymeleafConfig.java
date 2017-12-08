package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by yuweijun on 2017-07-31.
 */
@Configuration
public class ThymeleafConfig {

    // @Bean
    // public ServletContextTemplateResolver servletContextTemplateResolver() {
    //     ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver();
    //     servletContextTemplateResolver.setPrefix("classpath:/templates/");
    //     servletContextTemplateResolver.setSuffix(".html");
    //     servletContextTemplateResolver.setTemplateMode("HTML5");
    //     return servletContextTemplateResolver;
    // }

    // @Bean
    // public SpringTemplateEngine templateEngine(ServletContextTemplateResolver servletContextTemplateResolver) {
    //     SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
    //     springTemplateEngine.setTemplateResolver(servletContextTemplateResolver);
    //     return springTemplateEngine;
    // }

    // @Bean
    // public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
    //     ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
    //     thymeleafViewResolver.setTemplateEngine(templateEngine);
    //     return thymeleafViewResolver;
    // }

}
