package com.example.spring.jdbc.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author yuweijun 2017-03-15
 */
public class GuavaClassPathTest {

    @Test
    public void test1() throws IOException {
        ClassPath cp= ClassPath.from(Thread.currentThread().getContextClassLoader());
        ImmutableSet<ClassPath.ClassInfo> topLevelClassesRecursive = cp.getTopLevelClassesRecursive("org.springframework");
        for(ClassPath.ClassInfo info : topLevelClassesRecursive) {
            // Do stuff with classes here...
            // System.out.println(info.getName());
            try {
                Class<?> aClass = info.load();
                if (aClass.isInterface()) {
                    Method[] methods = aClass.getDeclaredMethods();
                    String simpleName = aClass.getSimpleName();
                    if (!"package-info".equals(simpleName)) {
                        System.out.println(methods.length + "\t\t" + info.getName());
                    }
                }
            } catch (Throwable e) {
                // System.out.println(e.getMessage());
            }
        }
    }

}
