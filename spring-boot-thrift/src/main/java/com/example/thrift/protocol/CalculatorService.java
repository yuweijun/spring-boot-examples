package com.example.thrift.protocol;

import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2016-06-13.
 */
@Component
public class CalculatorService {

    public int add(int num1, int num2) {
        return num1 + num2;
    }

    public int subtract(int num1, int num2) {
        return num1 - num2;
    }

    public int multiply(int num1, int num2) {
        return num1 * num2;
    }

    public int divide(int num1, int num2) {
        if (num2 == 0) {
            throw new IllegalArgumentException("num2 must not be zero");
        }

        return num1 / num2;
    }

}
