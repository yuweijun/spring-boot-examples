package com.example.spring.boot.mycat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author yuweijun
 * @date 2018-09-07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MycatApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test1() {
        List<Map<String, Object>> tables = jdbcTemplate.queryForList("show tables");
        tables.forEach(table -> {
            table.forEach((k, v) -> {
                System.out.println("k = " + k + ", v = " + v);
            });
        });
    }

}