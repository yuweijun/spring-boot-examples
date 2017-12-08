package com.example.config;

import java.util.Date;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.example.filters.MongoRequestFilter;

@Configuration
public class FiltersConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(FiltersConfiguration.class);

	@Bean
	public FilterRegistrationBean requestContextFilterRegistration() {
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(requestContextFilter());
		filter.setOrder(0);
		return filter;
	}

	@Bean
	public RequestContextFilter requestContextFilter() {
		return new RequestContextFilter();
	}

	@Bean
	public RequestContextListener requestContextListener() {
		// oauth2 sso login 需要注入这个Bean
		return new RequestContextListener();
	}

	@Bean
	public FilterRegistrationBean mongoFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		MongoRequestFilter mongoFilter = new MongoRequestFilter();
		registration.setFilter(mongoFilter);
		registration.addUrlPatterns("/mongo/*");
		registration.setName("mongo");
		LOGGER.info(MongoRequestFilter.class.toString() + "================" + new Date().getTime());

		return registration;
	}

	@Bean
	public FilterRegistrationBean shallowEtagHeaderFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ShallowEtagHeaderFilter());
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		registration.addUrlPatterns("/user/*");
		LOGGER.info(ShallowEtagHeaderFilter.class.toString() + "================" + new Date().getTime());
		return registration;
	}
}