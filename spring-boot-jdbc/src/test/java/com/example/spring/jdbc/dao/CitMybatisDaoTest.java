package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.City;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yuweijun 2016-06-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class CitMybatisDaoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CitMybatisDaoTest.class);

    @Autowired
    private CityMybatisDao cityMybatisDao;

    @Test
    public void selectCityById() {
        City city = this.cityMybatisDao.selectCityById(1);
        LOGGER.debug("{}", city);
    }

}