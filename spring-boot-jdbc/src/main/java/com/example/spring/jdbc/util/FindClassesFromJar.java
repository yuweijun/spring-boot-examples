package com.example.spring.jdbc.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author yuweijun 2017-03-15
 */
public class FindClassesFromJar {

    public static List<String> getClassNamesFromJar(String jarFile) {
        List<String> listofClasses = new ArrayList<>();
        try {
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile));
            JarEntry jar;

            while (true) {
                jar = jarInputStream.getNextJarEntry();
                if (jar == null) {
                    break;
                }
                if ((jar.getName().endsWith(".class"))) {
                    String className = jar.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    listofClasses.add(myClass);
                }
            }
        } catch (Exception e) {
            System.out.println("Oops.. Encounter an issue while parsing jar" + e.toString());
        }
        return listofClasses;
    }

    public static void mainTest(String[] args) {
        String home = System.getProperty("user.home");
        String pathToJar = home + "/.m2/repository/org/springframework/spring-beans/4.2.0.RELEASE/spring-beans-4.2.0.RELEASE.jar";
        List<String> myList = getClassNamesFromJar(pathToJar);
        for (String s : myList) {
            System.out.println(s);
        }
    }
}
