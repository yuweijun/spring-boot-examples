package com.example.spring.jdbc.service;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class PeopleJdbcServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PeopleJdbcService peopleJdbcService;

    @Test
    public void test1() {
        List<People> all = peopleJdbcService.findAll();
        all.stream().limit(10).forEach(System.out::println);
    }

    @Test
    public void test2() {
        People people = peopleJdbcService.findByFullName("test.yu");
        logger.info("{}", people);
    }

    @Test
    public void test3() {
        People people = new People();
        people.setFullName("test" + new Date().getTime());
        people.setJobTitle("manager");
        peopleJdbcService.save(people);
    }

    @Test
    public void test4() {
        List<People> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            People people = new People();
            people.setFullName("jdbc " + i);
            people.setJobTitle("jdbc " + i);
            customers.add(people);
        }
        peopleJdbcService.insertBatch(customers);
    }

    @Test
    public void test5() {
        People people = new People();
        people.setFullName("test.yu");
        People one = peopleJdbcService.findOne(people);
        logger.info("{}", one);
    }

}
