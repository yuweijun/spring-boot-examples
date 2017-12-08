package com.example.spring.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.Properties;

/**
 * @author yuweijun 2017-01-04
 */
@Configuration
public class AtomikosConfig {

    @Bean
    public AtomikosDataSourceBean atomikosDataSourceA() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("XA1DBMS");
        atomikosDataSourceBean.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://localhost:3306/jta1?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "dbuser");
        properties.put("password", "dbpass");
        atomikosDataSourceBean.setXaProperties(properties);
        atomikosDataSourceBean.setPoolSize(3);
        atomikosDataSourceBean.setMinPoolSize(3);
        atomikosDataSourceBean.setMaxPoolSize(5);
        return atomikosDataSourceBean;
    }

    @Bean
    public AtomikosDataSourceBean atomikosDataSourceB() {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName("XA2DBMS");
        atomikosDataSourceBean.setXaDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://localhost:3306/jta2?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "dbuser");
        properties.put("password", "dbpass");
        atomikosDataSourceBean.setXaProperties(properties);
        atomikosDataSourceBean.setPoolSize(3);
        atomikosDataSourceBean.setMinPoolSize(3);
        atomikosDataSourceBean.setMaxPoolSize(5);
        return atomikosDataSourceBean;
    }


    @Bean
    public JdbcTemplate jdbcTemplate1(@Qualifier("atomikosDataSourceA") DataSource atomikosDataSourceA) {
        return new JdbcTemplate(atomikosDataSourceA);
    }

    @Bean
    public JdbcTemplate jdbcTemplate2(@Qualifier("atomikosDataSourceB") DataSource atomikosDataSourceB) {
        return new JdbcTemplate(atomikosDataSourceB);
    }

    @Bean
    public UserTransactionManager userTransactionManager() {
        // 定义了AtomikosUserTransaction事务管理器
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        return userTransactionManager;
    }

    @Bean
    public UserTransaction userTransaction() throws SystemException {
        // 定义UserTransaction
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(30);
        return userTransaction;
    }

    /**
     * 加上 @Primary 避免2个同类型的bean产生冲突：
     * nouniquebeandefinitionexception no qualifying bean of type
     */
    @Primary
    @Bean(name = "jtaTransactionManager")
    public JtaTransactionManager jtaTransactionManager(UserTransaction userTransaction, UserTransactionManager
            userTransactionManager) {
        // 定义Spring事务管理器，
        // transactionManager属性指定外部事务管理器（真正的事务管理者），
        // 使用userTransaction指定UserTransaction，该属性一般用于本地JTA实现
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setUserTransaction(userTransaction);
        jtaTransactionManager.setTransactionManager(userTransactionManager);
        // org.springframework.transaction.InvalidIsolationLevelException:
        // JtaTransactionManager does not support custom isolation levels by default
        // - switch 'allowCustomIsolationLevels' to 'true'
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

}
