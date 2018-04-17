package com.example.spring.boot;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * Created by yuweijun on 2017-07-24.
 */
public class SpringClasspathBeanScannerTest {

    @Test
    public void example() {
        BeanDefinitionRegistry bdr = new SimpleBeanDefinitionRegistry();
        ClassPathBeanDefinitionScanner s = new ClassPathBeanDefinitionScanner(bdr);

        s.scan("com.example");
        String[] beans = bdr.getBeanDefinitionNames();
        for (int i = 0; i < beans.length; i++) {
            System.out.println(i + "\t:\t" + beans[i]);
        }
    }

}
