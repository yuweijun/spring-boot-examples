package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.mapper.UserMapper;
import com.example.spring.jdbc.model.User;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yuweijun 2016-06-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class SqlSessionTemplateTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SqlSession sqlSession;

    @Test
    public void test1() {
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getById(1);
            logger.debug("user is {}", user);
        } finally {
            try {
                // spring 管理的sqlSession就不需要手动关闭了。
                sqlSession.close();
            } catch (UnsupportedOperationException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
