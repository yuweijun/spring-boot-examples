package com.example.spring.jdbc.mapper;

import com.example.spring.jdbc.model.Hotel;
import com.example.spring.jdbc.test.util.SpringBootTransactionalTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yuweijun 2016-06-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class HotelMapperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelMapperTest.class);

    @Autowired
    private HotelMapper hotelMapper;

    @Test
    public void selectByCityId() throws Exception {
        Hotel hotel = hotelMapper.selectByCityId(1);
        LOGGER.info("hotel is : {}", hotel);
    }

    @Test
    public void findByName() {
        Hotel place = hotelMapper.findByName("Place");

        LOGGER.info("hotel is : {}", place);
    }

}