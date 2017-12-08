package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.service.PeopleJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PeopleJdbcTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJpaService.class);

    public int delete(People people) {
        return jdbcTemplate.update("delete from people where id = ?", people.getId());
    }

    public int save(People people) {
        String sql = "insert into people (full_name, job_title) values (?, ?)";
        int saved = jdbcTemplate.update(sql, new String[]{people.getFullName(), people.getJobTitle()});
        return saved;
    }

    /**
     * insert batch example
     */
    public void insertBatch(final List<People> peopleList) {
        String sql = "insert into people (full_name, job_title) values (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                People customer = peopleList.get(i);
                ps.setString(1, customer.getFullName());
                ps.setString(2, customer.getJobTitle());
            }

            @Override
            public int getBatchSize() {
                return peopleList.size();
            }
        });

    }

    public void insertBatchSQL(final String sql) {
        // insert batch example with SQL
        jdbcTemplate.batchUpdate(new String[]{sql});
    }

    public People findOne(int id) {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people where 1 = 1");
        sqls.add("and id = ?");
        String sql = sqls.stream().collect(Collectors.joining(" "));

        LOGGER.debug(sql);

        BeanPropertyRowMapper<People> rowMapper = BeanPropertyRowMapper.newInstance(People.class);

        List<People> list = jdbcTemplate.query(sql, rowMapper, id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<People> findAll() {
        List<String> sqls = new ArrayList<String>();
        sqls.add("select * from people");
        String sql = sqls.stream().collect(Collectors.joining(" "));

        LOGGER.debug(sql);
        BeanPropertyRowMapper<People> rowMapper = BeanPropertyRowMapper.newInstance(People.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int count() {
        String sql = "select count(*) from people";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
