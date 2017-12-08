package com.example.spring.jta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * http://www.importnew.com/18332.html
 *
 * @author yuweijun 2017-01-04
 */
@Service
public class TransactionTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionTemplateService.class);

    private static final String CREATE_TABLE_SQL = "create table if not exists test" +
            "(id int(4) not null auto_increment," +
            "name varchar(100), primary key (id))";
    private static final String DROP_TABLE_SQL = "drop table if exists test";
    private static final String INSERT_SQL = "insert into test(name) values(?)";
    private static final String COUNT_SQL = "select count(*) from test";

    /**
     * 必须用@Qualifier说明bean的名字
     */
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    /**
     * 必须用@Qualifier说明bean的名字
     */
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate secondaryJdbcTemplate;

    @Autowired
    @Qualifier("jtaTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Transactional
    public void test(boolean clean, boolean create) {
        LOGGER.info("transactionManager hashCode is : {}", transactionManager);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        if (clean) {
            primaryJdbcTemplate.execute(DROP_TABLE_SQL);
            secondaryJdbcTemplate.execute(DROP_TABLE_SQL);
        }
        if (create) {
            primaryJdbcTemplate.update(CREATE_TABLE_SQL);
            LOGGER.info("create table for secondary database : {}", CREATE_TABLE_SQL);
            secondaryJdbcTemplate.update(CREATE_TABLE_SQL);
        }

        int originalCount = primaryJdbcTemplate.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("original count of jta1 is : " + originalCount);
        try {
            transactionTemplate.execute((status) -> {
                primaryJdbcTemplate.update(INSERT_SQL, "test");
                // 如果数据库jta2没有创建，数据库表因此会回滚事务
                secondaryJdbcTemplate.update(INSERT_SQL, "test");
                return null;
            });
        } catch (RuntimeException e) {
            LOGGER.error("throw runtime exception when execute stransaction.", e);
        }

        int count1 = primaryJdbcTemplate.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta1 is " + count1);

        int count2 = primaryJdbcTemplate.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta2 is " + count2);
    }

}
