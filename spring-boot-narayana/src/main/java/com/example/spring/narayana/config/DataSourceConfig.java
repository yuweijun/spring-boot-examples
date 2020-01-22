package com.example.spring.narayana.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author Weijun Yu
 * @since 2020-01-22
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName("db1").
                build();
    }

    @Bean
    public DataSource dataSource2() {
        return new EmbeddedDatabaseBuilder().
                setType(EmbeddedDatabaseType.H2).
                setName("db2").
                setScriptEncoding("UTF-8").
                addScript("db2.sql").
                build();
    }

    @Bean
    @Primary
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("dataSource") DataSource primaryDataSource) {
        return new JdbcTemplate(primaryDataSource);
    }

    @Bean
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("dataSource2") DataSource secondaryDataSource) {
        return new JdbcTemplate(secondaryDataSource);
    }

}
