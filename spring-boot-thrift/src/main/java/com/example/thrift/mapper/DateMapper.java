package com.example.thrift.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yuweijun 2016-11-02
 */
public class DateMapper {

    public String map(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) : null;
    }

    public Date map(String date) {
        try {
            return date != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Date map(long time) {
        return new Date(time);
    }

}
