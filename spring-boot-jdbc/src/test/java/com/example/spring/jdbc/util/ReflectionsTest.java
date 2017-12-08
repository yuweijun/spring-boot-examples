package com.example.spring.jdbc.util;

import com.example.spring.jdbc.dao.PeopleJdbcDao;
import org.junit.Test;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author yuweijun 2017-03-14
 */
public class ReflectionsTest {

    @Test
    public void test() {
        Reflections reflections = new Reflections("com.example");

        Set<Class<? extends PeopleJdbcDao>> subTypes = reflections.getSubTypesOf(PeopleJdbcDao.class);
        for (Class<? extends PeopleJdbcDao> subType : subTypes) {
            System.out.println(subType);
        }
    }

    @Test
    public void test2() {
        Reflections reflections = new Reflections("com.example");

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> aClass : annotated) {
            System.out.println(aClass);
        }
    }

}
