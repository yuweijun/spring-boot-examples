package com.example.spring.jdbc.dao;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * "main@1" prio=5 tid=0x1 nid=NA runnable
 * java.lang.Thread.State: RUNNABLE
 * at com.example.spring.jdbc.dao.JdbcStatementTest.test1(JdbcStatementTest.java:27)
 * at sun.reflect.NativeMethodAccessorImpl.invoke0(NativeMethodAccessorImpl.java:-1)
 * at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
 * at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
 * at java.lang.reflect.Method.invoke(Method.java:498)
 * at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
 * at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
 * at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
 * at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
 * at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
 * at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
 * at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
 * at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
 * at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
 * at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
 * at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
 * at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
 * at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
 * at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
 * at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
 * at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
 * at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
 * at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
 * <p>
 * "Abandoned connection cleanup thread@827" daemon prio=5 tid=0xc nid=NA waiting
 * java.lang.Thread.State: WAITING
 * at java.lang.Object.wait(Object.java:-1)
 * at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
 * at com.mysql.jdbc.AbandonedConnectionCleanupThread.run(AbandonedConnectionCleanupThread.java:64)
 * at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
 * at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
 * at java.lang.Thread.run(Thread.java:745)
 *
 * @author yuweijun 2018-01-25.
 */
public class JdbcStatementTest {

    @Test
    public void test1() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbc", "dbuser", "dbpass");
            statement = connection.createStatement();
            // statement.setQueryTimeout(3);
            statement.execute("select 1 from dual");

            // debugger break point
            throw new java.lang.Exception(new com.mysql.jdbc.exceptions.MySQLTimeoutException());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } finally {
            // debugger break point
            System.out.println("finally");
        }
    }

    @Test
    public void test2() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbc", "dbuser", "dbpass");
            statement = connection.createStatement();
            statement.setQueryTimeout(3);
            System.out.println("Start Time: " + System.currentTimeMillis());
            // DB will wait for 10 Seconds
            statement.executeQuery("SELECT SLEEP(10)");
        } catch (Exception e) {
            // will throw exception after statement query timeout: 3 seconds
            System.out.println("End   Time: " + System.currentTimeMillis());
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } finally {
            // debugger break point
            System.out.println("finally");
        }
    }

}
