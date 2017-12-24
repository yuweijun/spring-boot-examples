package com.example.config;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;

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
 * 	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
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
		viewResolver.setPrefix("/WEB-INF/views/boot/");
		viewResolver.setSuffix(".boot");
		return viewResolver;
	}

	@Bean
	public MessageSource messageSource() {
		logger.info("messageSource");
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		logger.info("localeChangeInterceptor");
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		return localeChangeInterceptor;
	}

	@Bean
	public LocaleResolver localeResolver() {
		// 基于cookie的本地化资源处理器
		logger.info("localeResolver");
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(Locale.CHINA);
		return cookieLocaleResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
