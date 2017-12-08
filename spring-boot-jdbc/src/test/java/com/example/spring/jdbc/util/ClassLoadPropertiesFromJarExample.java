package com.example.spring.jdbc.util;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 从jar包中读取配置文件内容
 *
 * @author yuweijun 2016-09-19
 */
public class ClassLoadPropertiesFromJarExample {

    @Test
    public void test() throws IOException {
        Properties properties = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("com/mysql/jdbc/TimeZoneMapping.properties");
        System.out.println(is);
        properties.load(is);

        properties.forEach((k, v) -> {
            System.out.println(k + " => " + v);
        });
    }

}
