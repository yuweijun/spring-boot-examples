package com.example.spring.post.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Created by yuweijun on 2018-01-15.
 */
public class BeanFactoryPostProcessorExample implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactoryPostProcessorExample.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanExampleDefinition = beanFactory.getBeanDefinition("beanExample");
        beanExampleDefinition.setLazyInit(true);
        // org.springframework.beans.factory.support.DefaultListableBeanFactory
        LOGGER.info("bean factory : {}", beanFactory);
    }

}
