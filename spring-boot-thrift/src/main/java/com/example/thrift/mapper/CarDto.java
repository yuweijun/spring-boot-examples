package com.example.thrift.mapper;

import java.util.Date;

/**
 * @author yuweijun 2016-11-01
 */
public class CarDto {

    private String make;
    private int seatCount;
    private String type;
    private Date time;
    private String date;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CarDto{" +
                "make='" + make + '\'' +
                ", seatCount=" + seatCount +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", date='" + date + '\'' +
                '}';
    }

}
