package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;

import java.util.List;

/**
 * @author yuweijun 2016-06-09.
 */
public interface PeopleJdbcDao {

    int save(People people);

    People findOne(People people);

    List<People> findAll();

    long insertBatch(List<People> peopleList);

    People findByFullName(String fullName);

}
