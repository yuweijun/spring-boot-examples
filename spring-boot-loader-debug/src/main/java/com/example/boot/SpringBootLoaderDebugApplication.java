package com.example.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * at com.example.boot.SpringBootLoaderDebugApplication.main(SpringBootLoaderDebugApplication.java:13)
 * at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java:-1)
 * at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * at java.lang.reflect.Method.invoke(Method.java:498)
 * at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48)
 * at org.springframework.boot.loader.Launcher.launch(Launcher.java:87)
 * at org.springframework.boot.loader.Launcher.launch(Launcher.java:50)
 * at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:51)
 *
 * @author yuweijun 2017-12-18
 */
@SpringBootApplication
public class SpringBootLoaderDebugApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootLoaderDebugApplication.class).run(args);
    }

}
