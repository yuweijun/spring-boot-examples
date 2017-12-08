package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByUsername(String username);

	Optional<User> findOneByEmail(String email);

}