package com.example.spring.post.processor;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by yuweijun on 2018-01-15.
 */
public class BeanExample implements InitializingBean, DisposableBean {

    private String name;

    private int age;

    public BeanExample() {
        System.out.println();
        System.out.println("BeanExample constructor");
        System.out.println();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println();
        System.out.println("BeanExample set name");
        System.out.println();
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        System.out.println();
        System.out.println("BeanExample set age");
        System.out.println();
        this.age = age;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println();
        System.out.println("BeanExample destroy");
        System.out.println();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
        System.out.println("BeanExample afterPropertiesSet");
        System.out.println();
    }

    @Override
    public String toString() {
        return "BeanExample{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

