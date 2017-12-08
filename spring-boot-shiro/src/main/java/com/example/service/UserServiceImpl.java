package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public User getByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        User findOne = userRepository.findOne(id);
        return findOne;
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll(new Sort("email"));
    }
}