package com.example.spring.jdbc.mapper;

import com.example.spring.jdbc.model.User;
import com.example.spring.jdbc.test.util.PageBean;
import com.example.spring.jdbc.util.SpringBootTransactionalTest;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author yuweijun 2016-06-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class UserMapperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapperTest.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByName() {
        User user = userMapper.findByName("test.yu");
        LOGGER.info("user find: {}", user);
    }

    @Test
    public void getById() {
        User user = userMapper.getById(1);
        LOGGER.info("user find: {}", user);
    }

    @Test
    // @Commit
    public void save() {
        IntStream.generate(() -> (int) (System.nanoTime() % 100)).limit(100).forEach((age) -> {
            userMapper.save("name " + System.nanoTime(), age);
        });
    }

    @Test
    public void findByAge() {
        List<User> users = userMapper.findByAge(10);
        users.stream().forEach((user) -> {
            LOGGER.info("{} is {}.", user.getName(), user.getAge());
        });
    }

    @Test
    public void pageHelper() {
        PageHelper.startPage(1, 20);
        List<User> users = userMapper.findByAge(30);
        users.stream().forEach((user) -> {
            LOGGER.info("{} is {}.", user.getName(), user.getAge());
        });

        PageBean<User> pageBean = new PageBean<>(users);
        int pages = pageBean.getPages();
        LOGGER.info("pages {}.", pages);
        LOGGER.debug("{}", pageBean);
    }

}