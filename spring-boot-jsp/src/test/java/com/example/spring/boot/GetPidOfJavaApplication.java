package com.example.spring.boot;

import org.junit.Test;

import java.lang.management.ManagementFactory;

/**
 * Created by yuweijun on 2017-12-19.
 */
public class GetPidOfJavaApplication {

    @Test
    public void pid() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            System.out.println(jvmName);
            System.out.println(jvmName.split("@")[0]);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

}
