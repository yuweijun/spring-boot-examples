package com.example.spring.aop.proxy;

import org.springframework.stereotype.Service;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class UserServiceImpl implements UserService {

    @Override
    public String loadUser() {
        return "test user";
    }

}
