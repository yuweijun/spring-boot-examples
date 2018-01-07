package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

/**
 * @author yuweijun 2017-04-05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class PeopleJpaDaoTest {

    @Autowired
    private PeopleJpaDao peopleJpaDao;

    @Test
    public void findByFullName() throws Exception {
        Collection<People> yu = peopleJpaDao.findByFullName("test.yu");
        System.out.println(yu);
    }

}