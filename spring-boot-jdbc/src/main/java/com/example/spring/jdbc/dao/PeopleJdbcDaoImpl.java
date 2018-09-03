package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuweijun 2016-06-09.
 */
@Repository
public class PeopleJdbcDaoImpl implements PeopleJdbcDao {

    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int save(People people) {
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(people);
        if (people.getId() == 0) {
            String sql = "insert into people (full_name, job_title) values (:fullName, :jobTitle)";
            return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        } else {
            String sql = "update people set full_name = :fullName, job_title = :jobTitle where id = :id";
            return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        }
    }

    @Override
    public People findOne(People people) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people where 1 = 1");
        if (people.getId() > 0) {
            sqls.add("and id = :id");
        }
        if (null != people.getFullName()) {
            sqls.add("and full_name = :fullName");
        }
        if (null != people.getJobTitle()) {
            sqls.add("and job_title = :jobTitle");
        }

        String sql = sqls.stream().collect(Collectors.joining(" "));
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(people);

        List<People> list = namedParameterJdbcTemplate.query(sql, sqlParameterSource, BeanPropertyRowMapper.newInstance(People.class));
        if (list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public List<People> findAll() {
        String sql = "select * from people";
        return namedParameterJdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(People.class));
    }

    @Override
    public long insertBatch(List<People> peopleList) {
        List<SqlParameterSource> list = new ArrayList<>();
        String sql = "insert into people (full_name, job_title) values (:fullName, :jobTitle)";
        for (People people : peopleList) {
            BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(people);
            list.add(sqlParameterSource);
        }
        SqlParameterSource[] batch = list.toArray(new SqlParameterSource[]{});
        int[] ints = namedParameterJdbcTemplate.batchUpdate(sql, batch);
        return Arrays.stream(ints).count();
    }

    @Override
    public People findByFullName(String fullName) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people where full_name = :full_name order by id desc limit 1");

        String sql = sqls.stream().collect(Collectors.joining(" "));
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("full_name", fullName);
        List<People> list = namedParameterJdbcTemplate.query(sql, sqlParameterSource, BeanPropertyRowMapper.newInstance(People.class));
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
