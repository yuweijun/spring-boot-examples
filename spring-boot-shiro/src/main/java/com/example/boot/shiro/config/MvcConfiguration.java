package com.example.boot.shiro.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 
 * <b>27.1.5 Static Content</b>
 * 
 * <p>
 * By default Spring Boot will serve static content from a directory called /static (or /public or /resources or
 * /META-INF/resources) in the classpath or from the root of the ServletContext. It uses the ResourceHttpRequestHandler
 * from Spring MVC so you can modify that behavior by adding your own WebMvcConfigurerAdapter and overriding the
 * addResourceHandlers method.
 * 
 * <pre>
 * <code>
 * &#64;Override
 * public void addResourceHandlers(ResourceHandlerRegistry registry) {
 *	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
 * }
 * </code>
 * </pre>
 * 
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger logger = Logger.getLogger(MvcConfiguration.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("addResourceHandlers");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/public/**").addResourceLocations("/public/");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
