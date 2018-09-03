package com.example.spring.jdbc.service;

import com.example.spring.jdbc.dao.PeopleJdbcDao;
import com.example.spring.jdbc.model.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
public class PeopleJdbcService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJdbcService.class);

    @Autowired
    private PeopleJdbcDao peopleJdbcDao;

    @PostConstruct
    private void postConstruct() {
        // we can do some special action for this protocol.
        LOGGER.info("{} @PostConstruct actions...........", this);
    }

    public List<People> findAll() {
        return peopleJdbcDao.findAll();
    }

    public People findOne(People people) {
        return peopleJdbcDao.findOne(people);
    }

    // PROPAGATION_REQUIRED,ISOLATION_DEFAULT
    @Transactional
    public void save(People people) {
        peopleJdbcDao.save(people);
    }

    public People findByFullName(String fullName) {
        People people = peopleJdbcDao.findByFullName(fullName);
        return people;
    }

    public long insertBatch(List<People> list) {
        return peopleJdbcDao.insertBatch(list);
    }

    public List<People> execute() {
        IntStream.range(0, 10).forEach(i -> {
            peopleJdbcDao.findAll();
            LOGGER.info("loop for : {}", i);
        });

        return peopleJdbcDao.findAll();
    }

    @Transactional
    public List<People> useTransactions() {
        return execute();
    }

    @Transactional(timeout = 15)
    public void executeTransactionTimeout() {
        LOGGER.info("test transactional timeout");
        People one = peopleJdbcDao.findByFullName("test.yu");
        long dateTime = new Date().getTime();
        one.setJobTitle("test dateTime is " + dateTime);
        peopleJdbcDao.save(one);
        // JdbcTemplate 只要最后一个SQL在事务时间内完成就可以，下面的延时不会导致事务失败
        try { TimeUnit.SECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
        LOGGER.info("execute finished at " + dateTime);
    }

}
