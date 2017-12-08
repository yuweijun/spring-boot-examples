package com.example.spring.jdbc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author yuweijun 2016-12-22
 */
@WebListener("This is only a demo listener")
public class SimpleListenerDemo implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("initialized ServletContextEvent");
        System.out.println(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("destory ServletContextEvent");
    }

}
