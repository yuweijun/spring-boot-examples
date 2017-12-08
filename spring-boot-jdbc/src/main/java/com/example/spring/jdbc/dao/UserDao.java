package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	Collection<User> findByName(String name);

}
