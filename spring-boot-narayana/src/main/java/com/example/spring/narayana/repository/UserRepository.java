package com.example.spring.narayana.repository;

import com.example.spring.narayana.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
