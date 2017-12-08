package com.example.spring.jdbc.config;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

/**
 * @author yuweijun 2017-02-24
 */
@Configuration
public class HibernateConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateConfig.class);

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

    // 另一种SessionFactory配置方式
    // @Bean
    // public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf){
    //     LOGGER.info("hemf : {}", hemf.getClass());
    //     return hemf.getSessionFactory();
    // }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sf) {
        return new HibernateTransactionManager(sf);
    }

}
