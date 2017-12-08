package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.User;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;

/**
 * http://www.importnew.com/18016.html
 * http://www.importnew.com/17939.html
 * http://www.importnew.com/18013.html
 *
 * @author yuweijun 2017-02-23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class HibernateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTest.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        String createTableSql = "create table test(id int(4) AUTO_INCREMENT PRIMARY KEY, name varchar(100))";
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        if (sessionFactory == null) {
            this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        } else {
            LOGGER.info("sessionFactory : {}", sessionFactory.getClass());
        }
        sessionFactory.openSession().createSQLQuery(createTableSql).executeUpdate();
    }

    @After
    public void tearDown() {
        String dropTableSql = "drop table test";
        sessionFactory.openSession().createSQLQuery(dropTableSql).executeUpdate();
    }

    @Test
    public void testFirst() {
        Session session = sessionFactory.openSession();
        try {
            User model = new User();
            model.setName("name");
            session.save(model);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            session.close();
        }
    }

}
