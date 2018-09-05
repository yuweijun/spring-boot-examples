package com.example.spring.boot.sharding.jpa.repository;

import com.example.spring.boot.sharding.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yuweijun
 * @date 2018-09-05
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
