package com.example.spring.jdbc.service;

import com.example.spring.jdbc.SpringJdbcApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * jdbc:mysql://localhost/test?sendFractionalSeconds=true
 *
 * http://louishust.github.io/mysql/2014/09/05/timestamp-bug/
 * <p>
 * DROP TABLE IF EXISTS `date_time`;
 * CREATE TABLE `date_time` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `t1` time(3),
 * `t2` timestamp(6),
 * `t3` datetime(1),
 * `t4` datetime,
 * `t5` timestamp(3) NULL DEFAULT 0,
 * `t6` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * `t7` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB;
 *
 * @author yuweijun 2016-07-09.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SpringJdbcApplication.class)
@WebIntegrationTest(randomPort = true)
@Transactional
public class MysqlTimestampPrecisionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlTimestampPrecisionTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testTimestamp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // now is 2016-07-09 17:57:11.207
        LOGGER.info("now is {}", now);
        jdbcTemplate.execute("truncate table date_time");

        /*
         * mysql> SELECT * FROM `date_time`\G
         * ************************** 1. row ***************************
         * id: 1
         * t1: 17:57:11.207
         * t2: 2016-07-09 17:57:11.207000
         * t3: 2016-07-09 17:57:11.2
         * t4: 2016-07-09 17:57:11
         * t5: 2016-07-09 17:57:11.207
         * t6: 2016-07-09 17:57:11
         * t7: 2016-07-09 17:57:11
         * 1 row in set (0.00 sec)
         */
        int inserted = jdbcTemplate.update("insert into date_time(t1, t2, t3, t4, t5, t6) values (?, ?, ?, ?, ?, ?)", now, now, now, now, now, now);
        Assert.assertEquals(1, inserted);

        int countByT2 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t2 = ?", Integer.class, 1, now);
        Assert.assertEquals(1, countByT2);
        int countByT3 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t3 = ?", Integer.class, 1, now);
        // Assert.assertEquals(0, countByT3);
        int countByT5 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t5 = ?", Integer.class, 1, now);
        Assert.assertEquals(1, countByT5);
    }

    @Test
    public void testDate() {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        // now is 2016-07-09 20:25:07.815
        LOGGER.info("now is {}", format.format(now));
        jdbcTemplate.execute("truncate table date_time");

        /*
         * mysql> SELECT * FROM `date_time`\G
         * ************************** 1. row ***************************
         * id: 1
         * t1: 20:25:07.815
         * t2: 2016-07-09 20:25:07.815000
         * t3: 2016-07-09 20:25:07.8
         * t4: 2016-07-09 20:25:08
         * t5: 2016-07-09 20:25:07.815
         * t6: 2016-07-09 20:25:08
         * t7: 2016-07-09 20:25:07
         * 1 row in set (0.00 sec)
         * -- t6和t7有点意思啊, 同时写入的, 一个是MySQL自动设置的值, 时间一个四舍五入为08, 自动设置的值居然是07秒。
         */
        int inserted = jdbcTemplate.update("insert into date_time(t1, t2, t3, t4, t5, t6) values (?, ?, ?, ?, ?, ?)", now, now, now, now, now, now);
        // insert 1 records
        LOGGER.info("insert {} records", inserted);

        int countByT2 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t2 = ?", Integer.class, 1, now);
        Assert.assertEquals(1, countByT2);
        int countByT3 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t3 = ?", Integer.class, 1, now);
        // Assert.assertEquals(0, countByT3);
        int countByT5 = jdbcTemplate.queryForObject("select count(*) from date_time where id = ? and t5 = ?", Integer.class, 1, now);
        Assert.assertEquals(1, countByT5);
    }
}
