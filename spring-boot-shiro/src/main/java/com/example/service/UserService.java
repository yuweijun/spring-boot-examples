package com.example.service;

import com.example.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface UserService {

	User getUserById(long id);

	Collection<User> findAll();

	User getByUsername(String username);

	void save(User user);

}
