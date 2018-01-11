package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class ProxyFactoryBeanExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactoryBeanExample.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(ProxyFactoryBeanConfig.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            LOGGER.info(beanDefinitionName);
        }

        BusinessInterface business = (BusinessInterface) annotationConfigApplicationContext.getBean("businessImpl");
        business.doSomething();
        LOGGER.info(business.toString());
        LOGGER.info("hash code : {}", business.hashCode());

        ProxyFactoryBean proxyFactoryBean = annotationConfigApplicationContext.getBean(ProxyFactoryBean.class);
        if (proxyFactoryBean.isProxyTargetClass()) {
            // proxyFactoryBean.setProxyTargetClass(true);
            // Exception in thread "main" org.springframework.beans.factory.NoUniqueBeanDefinitionException:
            // No qualifying bean of type 'com.example.spring.aop.proxy.BusinessImplV1' available:
            // expected single matching bean but found 2: businessImplV1,businessImpl
            LOGGER.info("proxyFactoryBean.isProxyTargetClass() : {}", proxyFactoryBean.isProxyTargetClass());
            BusinessImplV1 businessImplV1 = annotationConfigApplicationContext.getBean(BusinessImplV1.class);
            businessImplV1.doSomething();
        } else {
            LOGGER.info("proxyFactoryBean.isProxyTargetClass() : {}", proxyFactoryBean.isProxyTargetClass());
            BusinessImplV1 bean = annotationConfigApplicationContext.getBean(BusinessImplV1.class);
            bean.doSomething();
        }
    }

}
