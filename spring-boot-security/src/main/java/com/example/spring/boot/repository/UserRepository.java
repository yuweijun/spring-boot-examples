package com.example.spring.boot.repository;

import java.util.Optional;

import com.example.spring.boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByUsername(String username);

	Optional<User> findOneByEmail(String email);

}