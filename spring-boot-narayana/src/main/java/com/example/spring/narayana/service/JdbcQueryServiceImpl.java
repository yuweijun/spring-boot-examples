package com.example.spring.narayana.service;

import com.example.spring.narayana.model.Employee;
import com.example.spring.narayana.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jms.core.JmsTemplate;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcQueryServiceImpl.class);

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate secondaryJdbcTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * method invoke logs
     *
     * <pre>
     * 2020-01-22 15:40:33.774 DEBUG 39450 --- [nio-8080-exec-1] o.j.s.OpenEntityManagerInViewInterceptor : Opening JPA EntityManager in OpenEntityManagerInViewInterceptor
     * 2020-01-22 15:40:33.780 DEBUG 39450 --- [nio-8080-exec-1] o.s.t.jta.JtaTransactionManager          : Creating new transaction with name [com.example.spring.narayana.service.JdbcQueryServiceImpl.queryDatabases]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT; ''
     * 2020-01-22 15:40:33.785  INFO 39450 --- [nio-8080-exec-1] c.e.s.n.service.JdbcQueryServiceImpl     : query users
     * 2020-01-22 15:40:33.789  INFO 39450 --- [nio-8080-exec-1] c.e.s.n.service.JdbcQueryServiceImpl     : query employees
     * 2020-01-22 15:40:33.790  INFO 39450 --- [nio-8080-exec-1] c.e.s.n.service.JdbcQueryServiceImpl     : jms send start
     * 2020-01-22 15:40:33.800  INFO 39450 --- [nio-8080-exec-1] c.e.s.n.service.JdbcQueryServiceImpl     : jms send finished
     * 2020-01-22 15:40:33.802 DEBUG 39450 --- [nio-8080-exec-1] o.s.t.jta.JtaTransactionManager          : Initiating transaction commit
     * 2020-01-22 15:40:33.805  INFO 39450 --- [enerContainer-1] c.e.s.narayana.listener.MessageListener  : query ----> jdbc template query finished.
     * </pre>
     */
    @Override
    @Transactional
    public Map<String, Object> queryDatabases() {
        LOGGER.info("query users");
        List<User> users = primaryJdbcTemplate.query("select * from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setBudget(rs.getInt("budget"));
                return user;
            }
        });

        LOGGER.info("query employees");
        List<Employee> employees = secondaryJdbcTemplate.query("select * from employees", (rs, num) -> {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            return employee;
        });

        LOGGER.info("jms send start");
        jmsTemplate.convertAndSend("query", "jdbc template query finished.");
        LOGGER.info("jms send finished");

        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
        map.put("employees", employees);
        return map;
    }

}
