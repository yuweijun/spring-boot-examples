package com.example.spring.jdbc.util;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * @author yuweijun 2017-03-15
 */
public class SpringClassPathBeanScannerTest {

    @Test
    public void test() {
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);

        s.scan("com.example");
        String[] beans = bdr.getBeanDefinitionNames();
        for (String bean : beans) {
            System.out.println(bean);
        }
    }


}
