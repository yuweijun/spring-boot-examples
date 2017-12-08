package com.example.thrift;

import com.example.thrift.mapper.Car;
import com.example.thrift.mapper.CarDto;
import com.example.thrift.mapper.CarMapper;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author yuweijun 2016-11-01
 */
public class MapStructTest {

    /**
     * 运行下面的测试，必须在命令行里先执行mvn compile
     * 生成CarMapperImpl实现类
     */
    @Test
    public void shouldMapCarToDto() {
        Car car = new Car();
        car.setMake("Morris");
        car.setNumberOfSeats(5);
        car.setType("SEDAN");
        Date date = new Date();
        car.setTime(date.getTime());
        car.setDate(date);

        CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        assertNotNull(carDto);
        assertEquals("Morris", carDto.getMake());
        assertEquals(5, carDto.getSeatCount());
        assertEquals("SEDAN", carDto.getType());
        assertEquals(date.getTime(), carDto.getTime().getTime());
        assertEquals(carDto.getDate(), new DateTime(date).toString("yyyy-MM-dd HH:mm:ss"));

        System.out.println(carDto);
    }

}

