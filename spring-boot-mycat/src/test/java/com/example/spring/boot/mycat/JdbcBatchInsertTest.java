package com.example.spring.boot.mycat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuweijun
 * @date 2018-09-07
 */
public class JdbcBatchInsertTest {

    private static JdbcTemplate jdbcTemplate;

    private static final AtomicInteger id = new AtomicInteger();

    private static ExecutorService executorService = Executors.newFixedThreadPool(8);

    @BeforeClass
    public static void setUpClassOnlyRunOnce() {
        // for mycat
        String url = "jdbc:mysql://localhost:8066/dbtest?useSSL=false";
        String username = "root";
        String password = "test";
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        jdbcTemplate = new JdbcTemplate(dataSource);

        Integer count = jdbcTemplate.queryForObject("select max(id) from user", Integer.class);
        id.set(count);
    }

    @Test
    public void test1() throws InterruptedException {
        for (int loop = 0; loop < 10; loop++) {
            executorService.submit(this::batchInsert);
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(10, TimeUnit.MINUTES)) ;

        Integer count = jdbcTemplate.queryForObject("select count(*) from user", Integer.class);
        System.out.println("====================== " + count);
    }

    private void batchInsert() {
        String sql = "INSERT INTO user (id, name, age) VALUES (?, ?, ?)";
        System.out.println(new Date());
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id.incrementAndGet());
                ps.setString(2, "name " + System.currentTimeMillis());
                ps.setInt(3, (int) (System.nanoTime() % 100));
            }

            @Override
            public int getBatchSize() {
                return 1000;
            }
        });
    }
}
