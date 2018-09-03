package com.example.spring.jdbc.service;

import com.example.spring.jdbc.dao.PeopleJpaDao;
import com.example.spring.jdbc.model.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
public class PeopleJpaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJpaService.class);

    @Resource
    private PeopleJpaDao peopleJpaDao;

    public List<People> findAll() {
        return peopleJpaDao.findAll();
    }

    // PROPAGATION_REQUIRED,ISOLATION_DEFAULT
    @Transactional
    @CachePut("peopleJpa")
    @CacheEvict(value = {"peopleJpa"}, allEntries = true)
    public void save(People people) {
        peopleJpaDao.save(people);
    }

    @Cacheable("peopleJpa")
    public Collection<People> findByFullName(String fullName) {
        LOGGER.info("find from database for : {}", fullName);
        Collection<People> all = peopleJpaDao.findByFullName(fullName);
        return all;
    }

    public List<People> execute() {
        IntStream.range(0, 10).forEach(i -> {
            peopleJpaDao.findAll();
            LOGGER.info("loop for : {}", i);
        });

        return peopleJpaDao.findAll();
    }

    @Transactional
    public List<People> useTransactions() {
        return execute();
    }

    @Transactional(timeout = 15)
    public void executeTransactionTimeout(long timeout) {
        LOGGER.info("test transactional timeout");
        People one = peopleJpaDao.getOne(1);
        long dateTime = new Date().getTime();
        one.setJobTitle("test dateTime is " + dateTime);
        peopleJpaDao.save(one);
        // JPA 这里的延时会导致事务失败回滚，Transactional的timeout应该要包含下面的延时时间
        try { TimeUnit.SECONDS.sleep(timeout); } catch (InterruptedException e) { e.printStackTrace(); }
        LOGGER.info("execute finished at " + dateTime);
    }

}
