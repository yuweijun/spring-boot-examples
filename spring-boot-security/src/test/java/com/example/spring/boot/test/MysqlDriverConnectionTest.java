package com.example.spring.boot.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class MysqlDriverConnectionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlDriverConnectionTest.class);

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			LOGGER.info("加载MySQL驱动程序失败");
			e.printStackTrace();
		}

		// 在xml配置文件中，url中的&符号需要转义成&amp;
		String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DriverManager.getConnection(url, "dbuser", "dbpass");
		} catch (SQLException e) {
			LOGGER.info("数据库连接错误 ---------------");
			e.printStackTrace();
		}

		if (connection != null) {
			try {
				statement = connection.createStatement();
				resultSet = statement.executeQuery("select * from user");

				while (resultSet.next()) {
					// columnIndex the first column is 1, the second is 2, ...
					LOGGER.info("{}", resultSet.getInt(1));
					LOGGER.info(resultSet.getString(2));
				}
			} catch (SQLException e) {
				LOGGER.info("数据操作错误 -------------");
				e.printStackTrace();
			} finally {
				try {
					if (resultSet != null) {
						resultSet.close();
					}
				} catch (SQLException e) {
					LOGGER.info("resultSet close exception ================");
					e.printStackTrace();
				}
				try {
					if (statement != null) {
						statement.close();
					}
				} catch (SQLException e) {
					LOGGER.info("statement close exception ================");
					e.printStackTrace();
				}
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					LOGGER.info("connection close exception ================");
					e.printStackTrace();
				}
			}
		}

	}

}
