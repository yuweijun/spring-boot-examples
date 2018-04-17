package com.example.spring.boot.service;

import com.example.spring.boot.dto.UserCreateForm;
import com.example.spring.boot.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface UserService {

	User getUserById(long id);

	Optional<User> getUserByEmail(String email);

	Collection<User> findAll();

	User create(UserCreateForm form);

	User createInNewTransaction(UserCreateForm form);

	Optional<User> getByUsername(String username);

	void save(User user);

}
