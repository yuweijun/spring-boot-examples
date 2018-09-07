package com.example.spring.jdbc.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yuweijun
 * @date 2018-09-07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTemplateBatchInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);


    @Test
    public void test1() throws InterruptedException {
        for (int loop = 0; loop < 10000; loop++) {
            executorService.submit(this::batchInsert);
        }
        executorService.shutdown();
        while(!executorService.awaitTermination(10, TimeUnit.MINUTES));
    }

    private void batchInsert() {
        String sql = "INSERT INTO user (name, age) VALUES (?, ?)";
        System.out.println(new Date());
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "name " + i);
                ps.setInt(2, i);
            }

            @Override
            public int getBatchSize() {
                return 1000;
            }
        });
    }
}
