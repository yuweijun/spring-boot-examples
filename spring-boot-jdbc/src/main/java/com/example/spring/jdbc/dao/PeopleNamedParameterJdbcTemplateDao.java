package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.service.PeopleJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class PeopleNamedParameterJdbcTemplateDao extends NamedParameterJdbcDaoSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJpaService.class);

    @PostConstruct
    private void initialize() {
        // http://www.mkyong.com/spring/how-to-autowire-datasource-in-jdbcdaosupport/
        super.setJdbcTemplate(jdbcTemplate);
    }

    public int delete(People people) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", people.getId());
        return getNamedParameterJdbcTemplate().update("delete from people where id=:id", parameterSource);
    }

    public int saveByBeanProperty(People people) {
        String sql = "insert into people (full_name, job_title) values (:fullName, :jobTitle)";
        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(people);
        return getNamedParameterJdbcTemplate().update(sql, beanPropertySqlParameterSource);
    }

    public int save(People people) {
        String sql = "insert into people (full_name, job_title) values (:fullName, :jobTitle)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("fullName", people.getFullName());
        parameterSource.addValue("jobTitle", people.getJobTitle());
        return getNamedParameterJdbcTemplate().update(sql, parameterSource);
    }

    /**
     * insert batch example
     *
     * In fact, it is insert one by one in mysql.
     */
    public long insertBatch(List<People> peopleList) {
        String sql = "insert into people (full_name, job_title) values (:fullName, :jobTitle)";
        SqlParameterSource[] sqlParameterSources = SqlParameterSourceUtils.createBatch(peopleList.toArray());
        int[] ints = getNamedParameterJdbcTemplate().batchUpdate(sql, sqlParameterSources);
        logger.info(Arrays.stream(ints).boxed().collect(Collectors.toList()));
        IntStream intStream = Arrays.stream(ints);
        return intStream.count();
    }

    public People findOneByBeanPropertySqlParameterSource(People people) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people where 1=1");
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

        LOGGER.debug(sql);

        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(people);
        LOGGER.debug("people beanPropertySqlParameterSource is : {}", beanPropertySqlParameterSource);

        BeanPropertyRowMapper<People> rowMapper = BeanPropertyRowMapper.newInstance(People.class);

        return getNamedParameterJdbcTemplate().queryForObject(sql, beanPropertySqlParameterSource, rowMapper);
    }

    public People findOne(People people) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people where 1=1");
        if (people.getId() > 0) {
            sqls.add("and id = :id");
        }
        if (null != people.getFullName()) {
            sqls.add("and full_name = :full_name");
        }
        if (null != people.getJobTitle()) {
            sqls.add("and job_title = :job_title");
        }
        String sql = sqls.stream().collect(Collectors.joining(" "));

        LOGGER.debug(sql);

        BeanPropertyRowMapper<People> rowMapper = BeanPropertyRowMapper.newInstance(People.class);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("full_name", people.getFullName());
        parameterSource.addValue("job_title", people.getJobTitle());
        parameterSource.addValue("id", people.getId());

        return getNamedParameterJdbcTemplate().queryForObject(sql, parameterSource, rowMapper);
    }

    public List<People> findAll() {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people");
        String sql = sqls.stream().collect(Collectors.joining(" "));

        LOGGER.debug(sql);
        BeanPropertyRowMapper<People> rowMapper = BeanPropertyRowMapper.newInstance(People.class);
        return getNamedParameterJdbcTemplate().query(sql, rowMapper);
    }

    public int count(People people) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select count(*) from people where 1=1");
        if (people.getId() > 0) {
            sqls.add("and id = :id");
        }
        if (null != people.getFullName()) {
            sqls.add("and full_name = :full_name");
        }
        if (null != people.getJobTitle()) {
            sqls.add("and job_title = :job_title");
        }
        String sql = sqls.stream().collect(Collectors.joining(" "));

        MapSqlParameterSource parameterSource = new MapSqlParameterSource("full_name", people.getFullName());
        parameterSource.addValue("job_title", people.getJobTitle());
        parameterSource.addValue("id", people.getId());

        LOGGER.debug(sql);

        return getNamedParameterJdbcTemplate().queryForObject(sql, parameterSource, Integer.class);
    }

}
