package com.example.boot.shiro.repository;

import com.example.boot.shiro.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	User findOneByUsername(String username);

}