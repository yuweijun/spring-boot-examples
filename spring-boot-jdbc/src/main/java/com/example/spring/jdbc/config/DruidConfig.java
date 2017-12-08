package com.example.spring.jdbc.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatViewServlet%E9%85%8D%E7%BD%AE
 * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatFilter
 *
 * @author yuweijun 2016-06-11.
 */
@Configuration
public class DruidConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public ServletRegistrationBean statViewServlet() {
        logger.debug("register StatViewServlet for DRUID stat filter.");
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(statViewServlet, "/druid/*");
        Map<String,String> map = new HashMap();

        // 配置监控页面访问密码
        map.put("loginUsername", "admin");
        map.put("loginPassword", "password");

        // 在StatViewSerlvet输出的html页面中，有一个功能是Reset All，执行这个操作之后，会导致所有计数器清零，重新计数。你可以通过配置参数关闭它。
        map.put("resetEnable", "true");

        // 实际应该访问${context}/druid/login.html,这里程序它重定向到${context}/login.html了,这是个问题。
        registrationBean.setInitParameters(map);
        return registrationBean;
    }

}