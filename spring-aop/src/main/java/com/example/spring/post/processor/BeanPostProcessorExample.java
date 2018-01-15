package com.example.spring.post.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Created by yuweijun on 2018-01-15.
 */
public class BeanPostProcessorExample implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanPostProcessorExample.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        LOGGER.info("postProcessBeforeInitialization bean name : {} and bean : {}", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        LOGGER.info("postProcessAfterInitialization bean name : {} and bean : {}", beanName, bean);
        return bean;
    }

}
