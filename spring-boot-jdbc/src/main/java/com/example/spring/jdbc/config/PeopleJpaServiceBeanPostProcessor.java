package com.example.spring.jdbc.config;

import com.example.spring.jdbc.service.PeopleJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2016-06-10.
 */
@Component
public class PeopleJpaServiceBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJpaServiceBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PeopleJpaService) {
            LOGGER.info("special postProcessAfterInitialization actions for bean : {}", bean);
        }

        return bean;
    }

}
