package com.example.spring.config;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-jta-bitronix
 *
 * @author yuweijun 2017-01-04
 */
@Configuration
public class BitronixConfig {

    @Bean
    public PoolingDataSource xaDataSourceA() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setUniqueName("BITRONIX-XA-1-DBMS");
        poolingDataSource.setTestQuery("SELECT 1 FROM dual");
        poolingDataSource.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://localhost:3306/jta1?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "dbuser");
        properties.put("password", "dbpass");
        poolingDataSource.setAllowLocalTransactions(true);
        poolingDataSource.setDriverProperties(properties);
        poolingDataSource.setMinPoolSize(3);
        poolingDataSource.setMaxPoolSize(5);
        return poolingDataSource;
    }

    @Bean
    public PoolingDataSource xaDataSourceB() {
        PoolingDataSource poolingDataSource = new PoolingDataSource();
        poolingDataSource.setUniqueName("BITRONIX-XA-2-DBMS");
        poolingDataSource.setTestQuery("SELECT 1 FROM dual");
        poolingDataSource.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        Properties properties = new Properties();
        properties.put("URL", "jdbc:mysql://localhost:3306/jta2?useUnicode=true&amp;characterEncoding=utf-8");
        properties.put("user", "dbuser");
        properties.put("password", "dbpass");
        poolingDataSource.setAllowLocalTransactions(true);
        poolingDataSource.setDriverProperties(properties);
        poolingDataSource.setMinPoolSize(3);
        poolingDataSource.setMaxPoolSize(5);
        return poolingDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplateA(@Qualifier("xaDataSourceA") DataSource poolingDataSource) {
        return new JdbcTemplate(poolingDataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplateB(@Qualifier("xaDataSourceB") DataSource poolingDataSource) {
        return new JdbcTemplate(poolingDataSource);
    }

    @Bean
    public bitronix.tm.Configuration bitronixTMConfig() {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
        configuration.setDisableJmx(true);
        return configuration;
    }

    @Bean
    public BitronixTransactionManager bitronixTransactionManager(bitronix.tm.Configuration bitronixTMConfig) {
        // TransactionManagerServices.getConfiguration().setDisableJmx(true);
        BitronixTransactionManager transactionManager = TransactionManagerServices.getTransactionManager();
        return transactionManager;
    }

    @Bean(name = "jtaBitronixTransactionManager")
    public JtaTransactionManager jtaBitronixTransactionManager(BitronixTransactionManager
                                                                       bitronixTransactionManage) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setUserTransaction(bitronixTransactionManage);
        jtaTransactionManager.setTransactionManager(bitronixTransactionManage);
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

}
