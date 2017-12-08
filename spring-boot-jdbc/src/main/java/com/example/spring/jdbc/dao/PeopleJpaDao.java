package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PeopleJpaDao extends JpaRepository<People, Integer> {

	Collection<People> findByFullName(String fullName);

}
