package com.example.spring.jdbc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfig {

	@Autowired
	private DataSource dataSource;

	/**
	 * <p>
	 * 使用liquibase控制数据库的版本，另一个选择方案是flyway
	 * 
	 * <p>
	 * LiquiBase在执行changelog时，会在数据库中插入两张表：DATABASECHANGELOG和DATABASECHANGELOGLOCK，分别记录changelog的执行日志和锁日志。
	 * 
	 * LiquiBase在执行changelog中的changeSet时，会首先查看DATABASECHANGELOG表，如果已经执行过，则会跳过（除非changeSet的runAlways属性为true，后面会介绍），
	 * 如果没有执行过，则执行并记录changelog日志；
	 * 
	 * changelog中的一个changeSet对应一个事务，在changeSet执行完后commit，如果出现错误则rollback；
	 * 
	 * <p>
	 * 一般情况下，不会使用这种spring方式控制版本，因为这个可以完全独立于应用本身的，可以使用liquibase-maven-plugin插件，在命令行中操作版本会更合适一些。
	 * 
	 * <p>
	 * liquibase:rollback
	 * 
	 * rollback有3中形式，分别是：
	 * 
	 * - rollbackCount: 表示rollback的changeset的个数；
	 * 
	 * - rollbackDate：表示rollback到指定的日期；
	 * 
	 * - rollbackTag：表示rollback到指定的tag，需要使用LiquiBase在具体的时间点打上tag；
	 * 
	 * mvn liquibase:rollback -Dliquibase.rollbackDate="Jan 1, 2016"
	 * 
	 * The following commands will roll the database configuration back by 3 changesets and create a tag called
	 * "checkpoint":
	 * 
	 * mvn liquibase:rollback -Dliquibase.rollbackCount=3
	 * 
	 * mvn liquibase:tag -Dliquibase.tag=checkpoint
	 * 
	 * You can now update the database, and at any stage rollback to that point using the rollback tag:
	 * 
	 * mvn liquibase:rollback -Dliquibase.rollbackTag=checkpoint
	 * 
	 * or alternatively generate the rollback SQL:
	 * 
	 * mvn liquibase:rollbackSQL -Dliquibase.rollbackTag=checkpoint
	 * 
	 */
	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase base = new SpringLiquibase();
		base.setDataSource(dataSource);
		base.setChangeLog("classpath:db/db.changelog.master.xml");
		return base;
	}

}
