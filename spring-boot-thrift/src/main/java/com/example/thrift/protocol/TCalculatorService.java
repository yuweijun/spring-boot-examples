package com.example.thrift.protocol;

import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

/**
 * @author yuweijun 2016-06-13.
 */
@ThriftService
public interface TCalculatorService {

    @ThriftMethod
    int calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException;

}