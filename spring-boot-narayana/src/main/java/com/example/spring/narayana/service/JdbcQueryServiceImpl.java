package com.example.spring.narayana.service;

import com.example.spring.narayana.model.Employee;
import com.example.spring.narayana.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Weijun Yu
 * @since 2020-01-22
 */
@Service
public class JdbcQueryServiceImpl implements JdbcQueryService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate secondaryJdbcTemplate;

    @Override
    @Transactional
    public Map<String, Object> queryDatabases() {
        List<User> users = primaryJdbcTemplate.query("select * from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setBudget(rs.getInt("budget"));
                return user;
            }
        });

        List<Employee> employees = secondaryJdbcTemplate.query("select * from employees", (rs, num) -> {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            return employee;
        });

        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
        map.put("employees", employees);
        return map;
    }

}
