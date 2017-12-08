package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.User;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author yuweijun 2017-03-01
 */
@Repository
public class AopUserDao implements InitializingBean {

    @Autowired
    private UserDao userDao;

    private UserDao proxyUserDao;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(userDao);

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        Properties properties = new Properties();
        properties.setProperty("*", "PROPAGATION_REQUIRED");
        transactionInterceptor.setTransactionAttributes(properties);

        proxyFactory.addAdvice(transactionInterceptor);
        proxyUserDao = (UserDao) proxyFactory.getProxy();
    }

    public void save(User user) {
        proxyUserDao.save(user);
    }

}
