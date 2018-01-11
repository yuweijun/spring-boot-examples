package com.example.spring.aop.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class BusinessInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        doBusiness(invocation);
        return invocation.proceed();
    }

    private void doBusiness(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        String clazz = method.getDeclaringClass().getCanonicalName();
        LOGGER.info("{} invocation method => {}", clazz, method.getName());
    }

}
