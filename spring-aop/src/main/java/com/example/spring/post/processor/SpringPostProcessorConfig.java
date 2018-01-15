package com.example.spring.post.processor;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yuweijun on 2018-01-15.
 */
@Configuration
public class SpringPostProcessorConfig {

    @Bean
    public BeanExample beanExample() {
        BeanExample example = new BeanExample();
        example.setAge(18);
        example.setName("test");
        return example;
    }

    @Bean
    public BeanPostProcessorExample beanPostProcessorExample() {
        return new BeanPostProcessorExample();
    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BeanFactoryPostProcessorExample();
    }

}
