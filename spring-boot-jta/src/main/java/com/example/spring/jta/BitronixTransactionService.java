package com.example.spring.jta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

/**
 * https://dzone.com/articles/xa-transactions-2-phase-commit
 *
 * @author yuweijun 2017-03-02
 */
@Service
public class BitronixTransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitronixTransactionService.class);

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
    @Qualifier("jdbcTemplateA")
    private JdbcTemplate jdbcTemplateA;

    /**
     * 必须用@Qualifier说明bean的名字
     */
    @Autowired
    @Qualifier("jdbcTemplateB")
    private JdbcTemplate jdbcTemplateB;

    /**
     * 声明式事务模型: 通过 @{@link Transactional} 声明事务
     */
    @Transactional
    public void test() {
        int originalCount = jdbcTemplateA.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("original count of jta1 is : " + originalCount);
        long time = new Date().getTime();
        String name = "test " + time;
        jdbcTemplateA.update(INSERT_SQL, name);
        jdbcTemplateB.update(INSERT_SQL, name);

        int count1 = jdbcTemplateA.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta1 is " + count1);
        int count2 = jdbcTemplateA.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta2 is " + count2);
    }

    @Autowired
    @Qualifier("jtaBitronixTransactionManager")
    private JtaTransactionManager jtaTransactionManager;

    public void testBitronixTransactionManager() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("TestBitronixTransaction");
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = jtaTransactionManager.getTransaction(def);
        try {
            jdbcTemplateA.update(INSERT_SQL, "testBitronixTransactionManager");
            jdbcTemplateB.update(INSERT_SQL, "testBitronixTransactionManager");
            jtaTransactionManager.commit(status);
        } catch (RuntimeException e) {
            jtaTransactionManager.rollback(status);
        }
        LOGGER.info("testBitronixTransactionManager finished");
    }

}
