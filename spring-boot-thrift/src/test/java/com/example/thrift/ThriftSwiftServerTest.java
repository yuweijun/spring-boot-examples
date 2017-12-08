package com.example.thrift;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.HelloIface;
import com.example.thrift.protocol.TCalculatorService;
import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServiceProcessor;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yuweijun 2016-06-14
 */
public class ThriftSwiftServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftSwiftServerTest.class);

    public static void main(String[] args) throws TTransportException {
        LOGGER.info("run TMultiplexedProcessor and TServer listening {}.....", ServiceConfigurationConstants.SERVICE_PORT);

        HelloIface helloService = new HelloIface() {

            @Override
            public String helloString(String para) throws TException {
                return para;
            }

            @Override
            public int helloInt(int para) throws TException {
                return para;
            }

            @Override
            public boolean helloBoolean(boolean para) throws TException {
                return para;
            }

            @Override
            public void helloVoid() throws TException {
            }

            @Override
            public String helloNull() throws TException {
                return null;
            }

        };

        ThriftCodecManager thriftCodecManager = new ThriftCodecManager();
        TCalculatorService calculatorService = (num1, num2, op) -> 10;
        ThriftServiceProcessor calculatorServiceProcessor = new ThriftServiceProcessor(thriftCodecManager, Arrays.asList(), calculatorService);
        // TProcessor tProcessor = NiftyProcessorAdapters.processorToTProcessor(calculatorServiceProcessor);

        ThriftServiceProcessor helloServiceProcessor = new ThriftServiceProcessor(thriftCodecManager, Arrays.asList(), helloService);

        ExecutorService taskWorkerExecutor = Executors.newFixedThreadPool(2);
        ThriftServerDef serverDef = ThriftServerDef.newBuilder()
                .listen(ServiceConfigurationConstants.SERVICE_PORT)
                .withProcessor(helloServiceProcessor)
                // 这里只能注入一个service
                // .withProcessor(calculatorServiceProcessor)
                .using(taskWorkerExecutor)
                .build();

        ExecutorService bossExecutor = Executors.newCachedThreadPool();
        ExecutorService ioWorkerExecutor = Executors.newCachedThreadPool();

        NettyServerConfig serverConfig = NettyServerConfig.newBuilder()
                .setBossThreadExecutor(bossExecutor)
                .setWorkerThreadExecutor(ioWorkerExecutor)
                .build();

        ThriftServer server = new ThriftServer(serverConfig, serverDef);

        server.start();
    }

}
