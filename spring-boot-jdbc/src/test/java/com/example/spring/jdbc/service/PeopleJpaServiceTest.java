package com.example.spring.jdbc.service;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class PeopleJpaServiceTest {

    @Resource
    private PeopleJpaService peopleJpaService; // @Resource是使用实例属性名字注入,不是按类型Type注入的。

    @Test
    public void test1() {
        List<People> all = peopleJpaService.findAll();
        all.stream().limit(10).forEach(System.out::println);
    }

    @Test
    public void test2() {
        // query from db
        Collection<People> all = peopleJpaService.findByFullName("test.yu");
        all.stream().forEach(System.out::println);
    }

    @Test
    public void test3() {
        People people = new People();
        people.setFullName("full.name");
        people.setJobTitle("manager");
        peopleJpaService.save(people);
    }

    @Test
    public void test4() {
        // query form cache
        IntStream.range(1, 10).forEach(i -> peopleJpaService.findByFullName("test.yu"));
        IntStream.range(1, 10).forEach(i -> peopleJpaService.findByFullName("full.name"));
    }

}
