package com.example.spring.jdbc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuweijun 2016-08-01.
 */
public class HotelSqlBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotelSqlBuilder.class);

    // If not use @Param, you should be define same arguments with mapper method
    // If use @Param, you can define only arguments to be used
    public String findByName(@Param("name") final String name) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("hotel");
            if (name != null) {
                WHERE("name like '%' || #{value} || '%'");
            }
            ORDER_BY("city");
        }}.toString();
        LOGGER.info("sql : \n\n{};\n\nname : {}", sql);
        return sql;
    }

}
