package com.example.spring.jta;

import com.atomikos.jdbc.AtomikosDataSourceBean;
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

import javax.sql.DataSource;
import javax.transaction.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.springframework.jdbc.support.JdbcUtils.closeResultSet;

/**
 * http://www.importnew.com/18332.html
 * https://my.oschina.net/pingpangkuangmo/blog/413518
 * <p>
 * spring自己的一系列接口设计：
 * <p>
 * PlatformTransactionManager 事务管理器
 * TransactionDefinition 事务定义
 * TransactionStatus 事务状态
 * <p>
 * 三种事务模型如下：
 * <p>
 * 1. 本地事务模型
 * 2. 编程式事务模型
 * 3. 声明式事务模型
 *
 * @author yuweijun 2017-01-04
 */
@Service
public class AtomikosTransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtomikosTransactionService.class);

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
    @Qualifier("jdbcTemplate1")
    private JdbcTemplate jdbcTemplate1;

    /**
     * 必须用@Qualifier说明bean的名字
     */
    @Autowired
    @Qualifier("jdbcTemplate2")
    private JdbcTemplate jdbcTemplate2;

    /**
     * 声明式事务模型: 通过 @{@link Transactional} 声明事务
     */
    @Transactional
    public void test() {
        int originalCount = jdbcTemplate1.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("original count of jta1 is : " + originalCount);
        long time = new Date().getTime();
        String name = "test " + time;
        jdbcTemplate1.update(INSERT_SQL, name);
        jdbcTemplate2.update(INSERT_SQL, name);

        int count1 = jdbcTemplate1.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta1 is " + count1);

        int count2 = jdbcTemplate1.queryForObject(COUNT_SQL, Integer.class);
        LOGGER.info("count of jta2 is " + count2);
    }

    @Autowired
    @Qualifier("atomikosDataSourceA")
    private AtomikosDataSourceBean atomikosDataSourceBean;

    @Autowired
    private UserTransaction userTransaction;

    /**
     * 编程式事务模型:手动提交或者回滚事务，不用加 @{@link Transactional}
     */
    public void xaProramming() throws SQLException, SystemException, HeuristicRollbackException, RollbackException,
            NotSupportedException, HeuristicMixedException {
        LOGGER.info("xa programming");
        Connection conn = null;
        UserTransaction tx = null;
        try {
            LOGGER.info("begin transaction");
            tx = getUserTransaction();                                      // 1. 获取事务
            tx.begin();                                                     // 2. 开启JTA事务
            conn = getDataSource().getConnection();                         // 3. 获取JDBC
            String sql = "select * from test";                              // 4. 声明SQL
            PreparedStatement pstmt = conn.prepareStatement(sql);           // 5. 预编译SQL
            ResultSet rs = pstmt.executeQuery();                            // 6. 执行SQL
            process(rs);                                                    // 7. 处理结果集
            closeResultSet(rs);                                             // 8. 释放结果集
            tx.commit();                                                    // 9. 提交事务
            LOGGER.info("commit transaction");
        } catch (Exception e) {
            tx.rollback();                                                  // 10. 回滚事务
            throw e;
        } finally {
            conn.close();                                                   // 11. 关闭连接
        }
    }

    private void process(ResultSet rs) throws SQLException {
        while (rs.next()) {
            LOGGER.info("rs : {}", rs.getString("name"));
        }
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public DataSource getDataSource() {
        return atomikosDataSourceBean;
    }

    @Autowired
    @Qualifier("jtaTransactionManager")
    private JtaTransactionManager jtaTransactionManager;

    /**
     * Spring中的事务分为物理事务和逻辑事务；
     * <p>
     * 物理事务：就是底层数据库提供的事务支持，如JDBC或JTA提供的事务；
     * 逻辑事务：是Spring管理的事务，不同于物理事务，逻辑事务提供更丰富的控制，
     * 而且如果想得到Spring事务管理的好处，必须使用逻辑事务，
     * 因此在Spring中如果没特别强调一般就是逻辑事务。
     *
     * 编程式事务模型:下面代码中不用加 annotation: @{@link Transactional}，事务通过代码手工提交或者回滚
     */
    public void testPlatformTransactionManager() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        LOGGER.info("Creating new transaction with name ......");
        def.setName("transactionNameX");
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = jtaTransactionManager.getTransaction(def);
        try {
            jdbcTemplate1.update(INSERT_SQL, "test jdbc insert");
            jdbcTemplate2.update(INSERT_SQL, "test jdbc insert");
            jtaTransactionManager.commit(status);
        } catch (RuntimeException e) {
            jtaTransactionManager.rollback(status);
        }
        // 不能执行创建表语句和删除表语句
        // jdbcTemplate1.execute(DROP_TABLE_SQL);
        LOGGER.info("StatementCallback; uncategorized SQLException for SQL [drop table if exists test];");
    }

}
