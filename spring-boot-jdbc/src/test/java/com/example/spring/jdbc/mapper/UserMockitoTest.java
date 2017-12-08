package com.example.spring.jdbc.mapper;

import com.example.spring.jdbc.model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

/**
 * @author yuweijun 2016-09-07
 */
public class UserMockitoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMockitoTest.class);

    @Test
    public void test1() {
        UserMapper userMapper = mock(UserMapper.class);
        User user = new User();
        user.setName("test");
        user.setAge(18);
        when(userMapper.findByName("test")).thenReturn(user);
        User mockUser = userMapper.findByName("test");
        LOGGER.info("mockUser age is : {}", mockUser.getAge());
        verify(userMapper, times(1)).findByName("test");
    }

}

