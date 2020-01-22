package com.example.spring.narayana.repository;

import com.example.spring.narayana.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, String> {
}
