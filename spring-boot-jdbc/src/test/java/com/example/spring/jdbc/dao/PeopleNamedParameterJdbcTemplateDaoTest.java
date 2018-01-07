package com.example.spring.jdbc.dao;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html
 * <p>
 * {@link org.springframework.test.context.transaction.TransactionalTestExecutionListener}: provides transactional test execution with default rollback semantics
 * <p>
 * http://stackoverflow.com/questions/12626502/rollback-transaction-after-test
 * <p>
 * From: 10.3.5.4 Transaction management (bold mine):
 * <p>
 * In the TestContext framework, transactions are managed by the TransactionalTestExecutionListener.
 * Note that TransactionalTestExecutionListener is configured by default, even if you do not explicitly declare @TestExecutionListeners on your test class.
 * To enable support for transactions, however, you must provide a PlatformTransactionManager bean in the application context loaded by @ContextConfiguration semantics. In addition, you must declare @Transactional either at the class or method level for your tests.
 *
 * @author yuweijun 2016-06-09.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class PeopleNamedParameterJdbcTemplateDaoTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PeopleNamedParameterJdbcTemplateDao peopleNamedParameterJdbcTemplateDao;

    @Test
//    @Rollback(false)
    public void delete() throws Exception {
        List<People> all = peopleNamedParameterJdbcTemplateDao.findAll();
        People last = all.stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList()).get(0);
        logger.debug("last {}", last);
        int delete = peopleNamedParameterJdbcTemplateDao.delete(last);
        logger.info("{}", delete);
    }

    @Test
    public void saveByBeanProperty() throws Exception {
        People people = new People();
        people.setFullName("named jdbc bean 1");
        people.setJobTitle("named jdbc bean 1");
        int i = peopleNamedParameterJdbcTemplateDao.saveByBeanProperty(people);
        logger.info("{}", i);
    }

    @Test
    public void save() throws Exception {
        People people = new People();
        people.setFullName("named jdbc 1");
        people.setJobTitle("named jdbc 1");
        int i = peopleNamedParameterJdbcTemplateDao.save(people);
        logger.info("{}", i);
    }

    @Test
    public void insertBatch() throws Exception {
        List<People> customers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            People people = new People();
            people.setFullName("named jdbc " + i);
            people.setJobTitle("named jdbc " + i);
            customers.add(people);
        }
        long count = peopleNamedParameterJdbcTemplateDao.insertBatch(customers);
        logger.info("count is {}", count);
    }

    @Test
    public void findOne() throws Exception {
        People people = new People();
        people.setFullName("test.yu");
        People exists = peopleNamedParameterJdbcTemplateDao.findOne(people);
        if (exists != null) {
            logger.info(exists.getFullName());
        }
    }

    @Test
    public void findOneByBeanPropertySqlParameterSource() throws Exception {
        People people = new People();
        people.setFullName("test.yu");
        People exists = peopleNamedParameterJdbcTemplateDao.findOneByBeanPropertySqlParameterSource(people);
        if (exists != null) {
            logger.info(exists.getFullName());
        }
    }

    @Test
    public void findAll() throws Exception {
        List<People> all = peopleNamedParameterJdbcTemplateDao.findAll();
        all.stream().forEach(System.out::println);
    }

    @Test
    public void count() throws Exception {
        People people = new People();
        people.setFullName("test.yu");
        int count = peopleNamedParameterJdbcTemplateDao.count(people);
        logger.info("count {}", count);
    }

}