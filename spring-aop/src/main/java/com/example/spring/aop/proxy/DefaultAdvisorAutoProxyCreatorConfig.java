package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yuweijun on 2018-01-15.
 */
@Configuration
public class DefaultAdvisorAutoProxyCreatorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdvisorAutoProxyCreatorConfig.class);

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
    public BeforeAdviceExample beforeAdviceExample() {
        return new BeforeAdviceExample();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * DefaultAdvisorAutoProxyCreator将扫描上下文，
     * 寻找所有的Advistor(一个Advisor是一个切入点和一个通知的组成) ，
     * 将这些Advisor应用到所有符合切入点的Bean中。
     *
     * {@link org.springframework.aop.support.RegexpMethodPointcutAdvisor }
     *
     * @param beforeAdviceExample
     * @return
     */
    @Bean
    public NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor(BeforeAdviceExample beforeAdviceExample) {
        NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor = new NameMatchMethodPointcutAdvisor();
        nameMatchMethodPointcutAdvisor.setMappedName("doSomething");
        nameMatchMethodPointcutAdvisor.setAdvice(beforeAdviceExample);
        return nameMatchMethodPointcutAdvisor;
    }


}
