package com.example.neo4j.repository;

import com.example.neo4j.model.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * @author yuweijun
 * @date 2018-08-29.
 */
public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findByName(String name);
}