package com.example.spring.boot;

import com.example.spring.boot.interceptor.SessionInterceptor;

import com.google.gson.GsonBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
        builder.indentOutput(true);
        converters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create());
        converters.add(gsonHttpMessageConverter);
    }

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**");
    }

    /**
     * <pre>
     * spring boot 中已经注册下面这个 filter bean，所以可以直接使用 request scope 和 session scope
     *
     * {"bean":"requestContextFilter","aliases":[],"scope":"singleton","type":"org.springframework.boot.web.filter.OrderedRequestContextFilter","resource":"class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter.class]","dependencies":[]}
     *
     * 但是不能与 CommandLineRunner 一起使用，启动抛异常如下
     *
     * java.lang.IllegalStateException: Failed to execute CommandLineRunner
     *         at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:735)
     *         at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:716)
     *         at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:703)
     *         at org.springframework.boot.SpringApplication.run(SpringApplication.java:304)
     *         at org.springframework.boot.builder.SpringApplicationBuilder.run(SpringApplicationBuilder.java:134)
     *         at com.example.spring.boot.SpringBootMvcApplication.main(SpringBootMvcApplication.java:28)
     *         at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *         at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     *         at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     *         at java.lang.reflect.Method.invoke(Method.java:498)
     *         at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:49)
     *
     * Caused by: java.lang.IllegalStateException: No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet/DispatcherPortlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
     *         at org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes(RequestContextHolder.java:131)
     *         at org.springframework.web.context.request.AbstractRequestAttributesScope.get(AbstractRequestAttributesScope.java:41)
     *         at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:340)
     *         ... 14 common frames omitted
     * </pre>
     */
    public FilterRegistrationBean requestContextFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        RequestContextFilter requestContextFilter = new RequestContextFilter();
        filterRegistrationBean.setFilter(requestContextFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}