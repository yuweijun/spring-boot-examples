package com.example.thrift;

import com.example.thrift.mapper.Car;
import com.example.thrift.mapper.CarDto;
import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author yuweijun 2016-11-01
 */
public class DozerTest {

    @Test
    public void test() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        List<String> list = new ArrayList<>();
        list.add("dozerBeanMapping.xml");
        mapper.setMappingFiles(list);

        Car sourceObject = new Car();
        sourceObject.setMake("Morris");
        sourceObject.setNumberOfSeats(5);
        sourceObject.setType("SEDAN");
        Date date = new Date();
        sourceObject.setTime(date.getTime());
        sourceObject.setDate(date);

        CarDto destObject = mapper.map(sourceObject, CarDto.class);
        assertNotNull(destObject);
        assertEquals("Morris", destObject.getMake());
        assertEquals(5, destObject.getSeatCount());
        assertEquals("SEDAN", destObject.getType());
        assertEquals(date.getTime(), destObject.getTime().getTime());
        assertEquals(destObject.getDate(), new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"));

        System.out.println(destObject);
    }

}
