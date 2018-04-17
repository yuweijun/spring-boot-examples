package com.example.spring.boot.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * http://www.concretepage.com/spring/example_custom_factorybean_spring
 * <p>
 * https://spring.io/blog/2011/08/09/what-s-a-factorybean
 */
public class MyBeanFactoryBeanExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBeanFactoryBeanExample.class);

    public static void main(String[] args) {
        LOGGER.info("...Loading beans...");
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(MyBeanConfiguration.class);

        LOGGER.info("...fetch MyBean to get Object....");
        MyBean myBean1 = context.getBean(MyBean.class);
        myBean1.doTask();

        // 虽然工厂中其实返回的是同一个myBean对象，
        // 但是 MyBeanFactoryBean#isSingleton 方法返回true的话，spring就不会再来访问getObject方法，
        // 而是直接返回之前生成的myBean实例
        MyBean myBean2 = context.getBean(MyBean.class);
        myBean2.doTask();
        context.close();
    }
}

@Configuration
class MyBeanConfiguration {

    @Bean
    public MyBeanFactoryBean myBeanFactoryBean() {
        MyBeanFactoryBean myBeanFactoryBean = new MyBeanFactoryBean();
        return myBeanFactoryBean;
    }

}

class MyBeanFactoryBean implements FactoryBean<MyBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBeanFactoryBean.class);

    private MyBean myBean = new MyBean();

    @Override
    public MyBean getObject() throws Exception {
        LOGGER.info("inside getObject : {}", myBean);
        return myBean;
    }

    @Override
    public Class<? extends MyBean> getObjectType() {
        LOGGER.info("inside getObjectType");
        return myBean.getClass();
    }

    @Override
    public boolean isSingleton() {
        LOGGER.info("inside isSingleton");
        return false;
    }

}

class MyBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBean.class);

    public MyBean() {
        LOGGER.info("Object of MyBean is created.");
    }

    public void doTask() {
        LOGGER.info("do some task.");
    }

    @Override
    public String toString() {
        return "MyBean{id:" + this.hashCode() + "}";
    }

}