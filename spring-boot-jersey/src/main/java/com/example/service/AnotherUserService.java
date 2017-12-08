package com.example.service;

import com.example.dto.UserCreateForm;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yuweijun 2017-05-18
 */
@Service
public class AnotherUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnotherUserService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test(UserCreateForm form) {
        LOGGER.info("===================================enter exception method : 1");
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setRole(form.getRole());
        user.setPasswordHash(form.getPassword());
        userRepository.save(user);
        LOGGER.info("===================================enter exception method : 2");
        throw new RuntimeException("throw new exception");
    }

    @Transactional
    public void test2(UserCreateForm form) {
        LOGGER.info("===================================enter exception method : 1");
        User user = new User();
        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setRole(form.getRole());
        user.setPasswordHash(form.getPassword());
        userRepository.save(user);
        LOGGER.info("===================================enter exception method : 2");
        throw new RuntimeException("throw new exception");
    }

}
