package com.example.spring;

import com.example.spring.jta.AtomikosTransactionService;
import com.example.spring.jta.BitronixTransactionService;
import com.example.spring.jta.TransactionTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author yuweijun 2017-01-04
 */
@SpringBootApplication
public class JtaTransactionExample implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JtaTransactionExample.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        ApplicationContext ctx = SpringApplication.run(JtaTransactionExample.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        LOGGER.info("bean sizes is: " + beanNames.length);

        Thread.sleep(2000);
        ((Closeable) ctx).close();
    }

    @Autowired
    private TransactionTemplateService transactionTemplateService;

    @Autowired
    private AtomikosTransactionService atomikosTransactionService;

    @Autowired
    private BitronixTransactionService bitronixTransactionService;

    @Override
    public void run(String... args) throws Exception {
        transactionTemplateService.test(true, true);

        transactionTemplateService.test(false, true);

        LOGGER.info("===========================================");

        atomikosTransactionService.test();

        atomikosTransactionService.xaProramming();

        atomikosTransactionService.testPlatformTransactionManager();

        LOGGER.info("===========================================");

        // bitronix.tm.utils.ManagementRegistrar 会在日志中输出很多warn，可以忽略
        bitronixTransactionService.test();

        bitronixTransactionService.testBitronixTransactionManager();

        LOGGER.info("end run command");
    }

}
