package com.example.thrift;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.HelloIface;
import com.example.thrift.protocol.TCalculatorService;
import com.example.thrift.protocol.TDivisionByZeroException;
import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;
import org.apache.thrift.TException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author yuweijun 2016-06-14
 */
public class ThriftSwiftClientTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() throws ExecutionException, InterruptedException, TException, TDivisionByZeroException {

        ThriftClientManager clientManager = new ThriftClientManager();
        FramedClientConnector connector = new FramedClientConnector(HostAndPort.fromParts("localhost", ServiceConfigurationConstants.SERVICE_PORT));

        // 对应的 ThriftSwiftServerTest 中开放的 service
        HelloIface helloService = clientManager.createClient(connector, HelloIface.class).get();
        String s = helloService.helloString("test string");
        logger.debug(s);

        TCalculatorService calculatorService = clientManager.createClient(connector, TCalculatorService.class).get();
        // 这里只能注入一个service, 所以不能打开下面的注释
        // calculatorService.calculate(1, 2, TOperation.ADD);
    }

}
