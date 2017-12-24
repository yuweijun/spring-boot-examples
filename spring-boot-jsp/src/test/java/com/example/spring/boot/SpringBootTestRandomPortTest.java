package com.example.spring.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yuweijun on 2017-12-14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestRandomPortTest {

    @Autowired
    EmbeddedWebApplicationContext server;

    @LocalServerPort
    int port;

    @Test
    public void test1() {
        System.out.println(port);
        int containerPort = server.getEmbeddedServletContainer().getPort();
        System.out.println(containerPort);
    }

}
