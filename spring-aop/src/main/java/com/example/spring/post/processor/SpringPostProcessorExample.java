package com.example.spring.post.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class SpringPostProcessorExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringPostProcessorExample.class);

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(SpringPostProcessorConfig.class);
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            LOGGER.info(beanDefinitionName);
        }

        BeanExample beanExample = annotationConfigApplicationContext.getBean(BeanExample.class);
        beanExample.destroy();
    }

}
