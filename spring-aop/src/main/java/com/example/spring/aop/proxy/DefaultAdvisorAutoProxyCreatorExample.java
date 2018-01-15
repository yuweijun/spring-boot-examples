package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class DefaultAdvisorAutoProxyCreatorExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdvisorAutoProxyCreatorExample.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(DefaultAdvisorAutoProxyCreatorConfig.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            LOGGER.info(beanDefinitionName);
        }

        BusinessInterface businessImplV1 = (BusinessInterface) annotationConfigApplicationContext.getBean("businessImplV1");
        businessImplV1.doSomething();

        BusinessInterface businessImplV2 = (BusinessInterface) annotationConfigApplicationContext.getBean("businessImplV2");
        LOGGER.info("annotationConfigApplicationContext businessImplV2 hash code is {}", businessImplV2.hashCode());
        businessImplV2.doSomething();

        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = annotationConfigApplicationContext.getBean(DefaultAdvisorAutoProxyCreator.class);
        LOGGER.info(defaultAdvisorAutoProxyCreator.toString());

        UserService userService = (UserService) annotationConfigApplicationContext.getBean("userService");
        String user = userService.loadUser();
        LOGGER.info(user);

        // defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        if (defaultAdvisorAutoProxyCreator.isProxyTargetClass()) {
            // 当代理是基于CGLIB方式时，以下代码可运行，如果是基于JDK的动态代理机制，代理类实际并不扩展目标bean，只是实现相同的接口而已，
            // 因此并不能根据目标bean，从beanFactory中获取相应的bean
            BusinessImplV2 businessImplV21 = annotationConfigApplicationContext.getBean(BusinessImplV2.class);
            businessImplV21.doSomething();
        } else {
            // throw exception:
            // org.springframework.beans.factory.NoSuchBeanDefinitionException:
            // No qualifying bean of type 'com.example.spring.aop.proxy.BusinessImplV2' available
            BusinessImplV2 bean = annotationConfigApplicationContext.getBean(BusinessImplV2.class);
            bean.doSomething();
        }
    }

}
