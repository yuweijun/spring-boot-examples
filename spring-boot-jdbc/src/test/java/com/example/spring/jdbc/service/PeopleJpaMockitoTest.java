package com.example.spring.jdbc.service;

import com.example.spring.jdbc.dao.PeopleJpaDao;
import com.example.spring.jdbc.model.People;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.times;

/**
 * @author yuweijun 2016-09-07
 */
public class PeopleJpaMockitoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleJpaServiceTest.class);

    @InjectMocks
    private PeopleJpaService peopleJpaService;

    @Mock
    private PeopleJpaDao peopleJpaDao;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test1() {
        Collection<People> people = new ArrayList<People>();
        People yu = new People();
        yu.setFullName("yu");
        yu.setJobTitle("developer");
        people.add(yu);
        Mockito.when(peopleJpaService.findByFullName("yu")).thenReturn(people);
        Collection<People> list = peopleJpaService.findByFullName("yu");
        LOGGER.info("list size is : {} and yu's job is : {}", list.size(), list.iterator().next().getJobTitle());
        Mockito.verify(peopleJpaDao, times(1)).findByFullName("yu");
    }

}
