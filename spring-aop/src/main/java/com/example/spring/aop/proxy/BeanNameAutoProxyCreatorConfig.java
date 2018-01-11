package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yuweijun on 2018-01-11.
 */
@Configuration
public class BeanNameAutoProxyCreatorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanNameAutoProxyCreatorConfig.class);

    @Bean
    public BusinessInterface businessImplV1() {
        BusinessImplV1 businessImplV1 = new BusinessImplV1();
        LOGGER.info("businessImplV1 hash code is {}", businessImplV1.hashCode());
        return businessImplV1;
    }

    @Bean
    public BusinessInterface businessImplV2() {
        BusinessImplV2 businessImplV2 = new BusinessImplV2();
        LOGGER.info("businessImplV2 hash code is {}", businessImplV2.hashCode());
        return businessImplV2;
    }

    @Bean
    public BusinessInterceptor businessInterceptor() {
        return new BusinessInterceptor();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setBeanNames("businessImplV1", "businessImplV2", "userService");
        beanNameAutoProxyCreator.setInterceptorNames("businessInterceptor");
        // beanNameAutoProxyCreator.setProxyTargetClass(true);
        return beanNameAutoProxyCreator;
    }

}
