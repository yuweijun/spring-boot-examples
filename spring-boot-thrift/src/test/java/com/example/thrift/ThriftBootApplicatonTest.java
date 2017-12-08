package com.example.thrift;

import com.example.thrift.protocol.TCalculatorService;
import com.example.thrift.protocol.TOperation;
import com.example.thrift.util.SpringBootTransactionalTest;
import com.facebook.nifty.client.HttpClientConnector;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftClientManager;
import org.apache.thrift.protocol.TProtocolFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author yuweijun 2016-06-13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTransactionalTest
public class ThriftBootApplicatonTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftBootApplicatonTest.class);

    @Autowired
    protected TProtocolFactory protocolFactory;

    @Value("${local.server.port}")
    protected int port;

    @Autowired
    protected ThriftCodecManager thriftCodecManager;

    protected TCalculatorService client;

    @Before
    public void setUp() throws Exception {
        HttpClientConnector connector = new HttpClientConnector(URI.create("http://localhost:" + port + "/calculator/"));
        ThriftClientManager clientManager = new ThriftClientManager(thriftCodecManager);
        client = clientManager.createClient(connector, TCalculatorService.class).get();
    }

    @Test
    public void add() throws Exception {
        int calculate = client.calculate(2, 3, TOperation.ADD);
        LOGGER.info("{}", calculate);
        assertEquals(5, calculate);
    }

}