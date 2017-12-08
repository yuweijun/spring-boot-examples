package com.example.spring.jdbc.mapper;

/**
 * @author yuweijun 2016-06-11.
 */

import com.example.spring.jdbc.model.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;


@Mapper
public interface HotelMapper {

    Hotel selectByCityId(int cityId);

    @SelectProvider(method = "findByName", type = HotelSqlBuilder.class)
    Hotel findByName(String name);

}