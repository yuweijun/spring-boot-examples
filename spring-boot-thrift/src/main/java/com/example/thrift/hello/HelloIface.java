package com.example.thrift.hello;

import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;

/**
 * @author yuweijun 2016-06-14
 */
@ThriftService
public interface HelloIface {

    @ThriftMethod
    public String helloString(String para) throws org.apache.thrift.TException;

    @ThriftMethod
    public int helloInt(int para) throws org.apache.thrift.TException;

    @ThriftMethod
    public boolean helloBoolean(boolean para) throws org.apache.thrift.TException;

    @ThriftMethod
    public void helloVoid() throws org.apache.thrift.TException;

    @ThriftMethod
    public String helloNull() throws org.apache.thrift.TException;

}
