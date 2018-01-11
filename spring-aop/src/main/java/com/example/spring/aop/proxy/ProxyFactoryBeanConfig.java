package com.example.spring.aop.proxy;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by yuweijun on 2018-01-11.
 */
@Configuration
public class ProxyFactoryBeanConfig {

    @Bean
    public BusinessInterface businessImplV1() {
        return new BusinessImplV1();
    }

    @Bean
    public BusinessInterface businessImplV2() {
        return new BusinessImplV2();
    }

    @Bean
    public BusinessInterceptor businessInterceptor() {
        return new BusinessInterceptor();
    }

    @Bean
    public ProxyFactoryBean businessImpl() throws ClassNotFoundException {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setProxyInterfaces(new Class[]{BusinessInterface.class});
        proxyFactoryBean.setInterceptorNames("businessInterceptor");
        proxyFactoryBean.setTargetName("businessImplV1");
        proxyFactoryBean.setProxyTargetClass(true);
        return proxyFactoryBean;
    }

}
