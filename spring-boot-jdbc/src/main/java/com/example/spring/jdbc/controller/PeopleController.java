package com.example.spring.jdbc.controller;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.service.PeopleJdbcService;
import com.example.spring.jdbc.service.PeopleJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("people")
public class PeopleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    public PeopleJpaService peopleJpaService;

    @Autowired
    private PeopleJdbcService peopleJdbcService;

    @RequestMapping("/index")
    public Collection<People> index() {
        Collection<People> found = peopleJpaService.findByFullName("test.yu");
        return found;
    }

    @RequestMapping("/all")
    public List<People> all() throws InterruptedException {
        List<People> all = peopleJpaService.findAll();
        return all;
    }

    @GetMapping("/use/jpa/transactions")
    public List<People> jpaTransactions() throws InterruptedException {
        List<People> all = peopleJpaService.useTransactions();
        return all;
    }

    @GetMapping("/without/jpa/transactions")
    public List<People> jpa() throws InterruptedException {
        List<People> all = peopleJpaService.execute();
        return all;
    }

    @GetMapping("/use/jdbc/transactions")
    public List<People> jdbcTransactions() throws InterruptedException {
        List<People> all = peopleJdbcService.useTransactions();
        return all;
    }

    @GetMapping("/without/jdbc/transactions")
    public List<People> jdbc() throws InterruptedException {
        List<People> all = peopleJdbcService.execute();
        return all;
    }

    @GetMapping("/jpa/transactional/timeout")
    public String jpaTimeout() throws InterruptedException {
        // Caused by: javax.persistence.PersistenceException: null
        // 事务超时时间15秒小于延时的20秒时间，所以下面这个事务失败
        try {
            peopleJpaService.executeTransactionTimeout(20);
        } catch (Exception e) {
            LOGGER.error("sleep 20s for JPA transactional", e);
        }

        // 事务超时时间15秒大于延时的10秒时间，所以下面这个事务可以成功
        peopleJpaService.executeTransactionTimeout(10);
        return "timeout";
    }

    /**
     * spring JdbcTemplate 和 JPA 的事务超时时间处理并不一样，spring原生的JdbcTemplate感觉更合理一些。
     */
    @GetMapping("/jdbc/transactional/timeout")
    public String jdbcTimeout() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            peopleJdbcService.executeTransactionTimeout();
        });
        Thread thread2 = new Thread(() -> {
            peopleJdbcService.executeTransactionTimeout();
        });

        LOGGER.info("这2个线程中有一个因为并发更新无法提交而超时失败，一个可以成功");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        TimeUnit.SECONDS.sleep(1);

        return "timeout";
    }
}
