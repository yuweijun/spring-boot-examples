package com.example.thrift;

import com.example.thrift.protocol.TCalculatorService;
import com.example.thrift.protocol.TDivisionByZeroException;
import com.example.thrift.protocol.TOperation;
import com.facebook.nifty.client.HttpClientConnector;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftClientManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * @author yuweijun 2016-06-13
 */
public class CalculatorClientIndependencyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorClientIndependencyTest.class);

    @Before
    public void startApplication() {
        LOGGER.warn("======================================================================");
        LOGGER.warn("Please run spring boot web application - ThriftBootApplicaton firstly!");
        LOGGER.warn("======================================================================");
    }

    @Test
    public void client() throws ExecutionException, InterruptedException, TDivisionByZeroException {
        HttpClientConnector connector = new HttpClientConnector(URI.create("http://localhost:8080/calculator/"));
        ThriftCodecManager thriftCodecManager = new ThriftCodecManager();
        ThriftClientManager clientManager = new ThriftClientManager(thriftCodecManager);

        TCalculatorService client = clientManager.createClient(connector, TCalculatorService.class).get();
        assertEquals(5, client.calculate(2, 3, TOperation.ADD));
        assertEquals(3, client.calculate(5, 2, TOperation.SUBTRACT));
        assertEquals(10, client.calculate(5, 2, TOperation.MULTIPLY));
        assertEquals(2, client.calculate(10, 5, TOperation.DIVIDE));

        try {
            client.calculate(10, 0, TOperation.DIVIDE);
        } catch (TDivisionByZeroException e) {
            LOGGER.error("TDivisionByZeroException throwed", e);
        }
    }

}
