package com.example.spring.jsp;

import com.example.spring.jsp.listener.ExampleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootApplicationListenerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootApplicationListenerApplication.class)
                .listeners(new ExampleListener())
                .run(args);
    }

    /**
     * ApplicationArguments 接口即提供对原始 String[] 参数的访问，也提供对解析成 option 和 non-option 参数的访问
     *
     * @param applicationArguments 应用启动传入的参数
     */
    @Autowired
    public void arguments(ApplicationArguments applicationArguments) {
        boolean debug = applicationArguments.containsOption("debug");
        List<String> others = applicationArguments.getNonOptionArgs();
        // if run with "--debug a b c" debug=true, others=[ a, b, c]
        System.out.println("\n\n");
        System.out.println(debug);
        System.out.println(others);
        System.out.println("\n\n");
    }

    /**
     * 可以自己注册 TomcatEmbeddedServletContainerFactory , JettyEmbeddedServletContainerFactory
     * 或 UndertowEmbeddedServletContainerFactory 很多配置选项提供setter方法,
     * 有的甚至提供一些受保护的钩子方法以满足你的某些特殊需求,具体参考源码或相关文档。
     *
     * @return EmbeddedServletContainerFactory
     */
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(9000);
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
        return factory;
    }

}
