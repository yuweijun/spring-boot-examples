package com.example.spring.jdbc.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yuweijun 2017-03-15
 */
public class PrintClassesFromJar {

    public static void mainTest(String[] args) throws IOException {
        String home = System.getProperty("user.home");
        String pathToJar = home + "/.m2/repository/com/google/guava/guava/19.0/guava-19.0.jar";
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            try {
                Class c = cl.loadClass(className);
                String name = c.getName();
                // System.out.println(name);
                if (c.isInterface()) {
                    Method[] declaredMethods = c.getDeclaredMethods();
                    String simpleName = c.getSimpleName();
                    if (!"package-info".equals(simpleName)) {
                        System.out.println(declaredMethods.length + "\t\t" + c.getName());
                    }
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

}
