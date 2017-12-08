package com.example.spring.jdbc.mapper;

import com.example.spring.jdbc.model.User;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;

/**
 * @author yuweijun 2016-07-30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class MybatisLocalCacheTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisLocalCacheTest.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByName() {
        User user = userMapper.findByName("test.yu");
        LOGGER.info("user find: {}", user);
        // test mybatis local cache in sql session.
        // it will not query data from database, just return from local cache
        IntStream.range(1, 100).forEach(i -> userMapper.findByName("test.yu"));
    }

    @Test
    public void getById() {
        User user = userMapper.getById(1);
        LOGGER.info("user find: {}", user);

        // it will query data from database, test method run in different thread, and using difference mybatis sqlsession
        // console will output query sql information.
        User test = userMapper.findByName("test.yu");
        LOGGER.info("user find: {}", test);
    }

}
