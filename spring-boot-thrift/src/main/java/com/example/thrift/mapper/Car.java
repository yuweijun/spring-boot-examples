package com.example.thrift.mapper;

import java.util.Date;

/**
 * @author yuweijun 2016-11-01
 */
public class Car {

    private String make;
    private int numberOfSeats;
    private String type;
    private long time;
    private Date date;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
