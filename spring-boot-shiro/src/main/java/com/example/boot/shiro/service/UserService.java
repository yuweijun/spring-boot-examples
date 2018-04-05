package com.example.boot.shiro.service;

import java.util.Collection;

import com.example.boot.shiro.model.User;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

	User getUserById(long id);

	Collection<User> findAll();

	User getByUsername(String username);

	void save(User user);

}
