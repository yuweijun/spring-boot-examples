package com.example.spring.jdbc.jta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author yuweijun 2017-01-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JdbcRollbackTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void test() throws NamingException, SQLException {
        Connection conn = dataSource.getConnection();
        try {
            conn.setAutoCommit(false); // 更改JDBC事务的默认提交方式
            Statement statement = conn.createStatement();
            boolean executed = statement.execute("update people set full_name = 'yu' where id = 1");
            // true if the first result is a ResultSet object; false if it is an update count or there are no results
            System.out.println(executed);

            throw new RuntimeException("test jdbc rollback");
            // conn.commit(); // 提交JDBC事务
        } catch (Exception e) {
            try {
                System.out.println("begin rollback : " + e.getMessage());
                conn.rollback(); // 回滚JDBC事务
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

}
