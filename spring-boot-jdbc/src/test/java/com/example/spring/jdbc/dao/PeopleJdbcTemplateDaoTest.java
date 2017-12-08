package com.example.spring.jdbc.dao;

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
public class PeopleJdbcTemplateDaoTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PeopleJdbcTemplateDao peopleJdbcTemplateDao;

    @Test
    public void test1() {
        List<People> all = peopleJdbcTemplateDao.findAll();
        all.stream().forEach(System.out::println);
    }

    @Test
    public void test2() {
        int count = peopleJdbcTemplateDao.count();

        logger.info("count all: {}", count);

        People exists = peopleJdbcTemplateDao.findOne(1);
        logger.info(exists.getFullName());
    }

    @Test
    public void test3() {
        People people = new People();
        people.setFullName("test.yu");
        People exists = peopleJdbcTemplateDao.findOne(1);
        logger.info(exists.getJobTitle());
    }

    @Test
    public void test4() {
        People people = new People();
        long time = new Date().getTime();
        people.setFullName("jdbc " + time);
        people.setJobTitle("jdbc " + time);
        int saved = peopleJdbcTemplateDao.save(people);
        logger.info("{}", saved);
    }

    @Test
    public void test5() {
        List<People> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            People people = new People();
            people.setFullName("jdbc " + i);
            people.setJobTitle("jdbc " + i);
            customers.add(people);
        }
        peopleJdbcTemplateDao.insertBatch(customers);
    }

    @Test
    public void delete() {
		// List<People> all = peopleJdbcTemplateDao.findAll();
		// People last = all.stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList()).get(0);
        People first = peopleJdbcTemplateDao.findOne(1);
        logger.debug("last {} will be deleted and rollback............................................", first);
        peopleJdbcTemplateDao.delete(first);
    }

}
