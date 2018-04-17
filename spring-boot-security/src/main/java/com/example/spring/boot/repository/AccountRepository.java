package com.example.spring.boot.repository;

import java.util.Optional;

import com.example.spring.boot.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUsername(String username);
}