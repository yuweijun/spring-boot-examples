package com.example.spring.jdbc.core;

import org.junit.Test;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;

/**
 * @author yuweijun 2017-03-24
 */
public class ResolvableTypeTest {

    private HashMap<Integer, List<String>> myMap;

    @Test
    public void test() throws NoSuchFieldException {
        ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
        ResolvableType superType = t.getSuperType();// AbstractMap<Integer, List<String>>
        System.out.println(superType);
        t.asMap(); // Map<Integer, List<String>>
        t.getGeneric(0).resolve(); // Integer
        t.getGeneric(1).resolve(); // List
        t.getGeneric(1); // List<String>
        t.resolveGeneric(1, 0); // String
    }

}
