package com.example.thrift;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.Hello;
import com.example.thrift.hello.HelloServiceImpl;
import com.example.thrift.protocol.TCalculatorService;
import com.example.thrift.tutorial.Calculator;
import com.example.thrift.tutorial.CalculatorImpl;
import com.facebook.swift.codec.ThriftCodecManager;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yuweijun 2016-06-13
 */
@SpringBootApplication
public class ThriftBootApplicaton implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftBootApplicaton.class);

    public static void main(String[] args) {
        SpringApplication.run(ThriftBootApplicaton.class, args);
    }

    @Autowired
    private TProtocolFactory protocolFactory;

    @Autowired
    private HelloServiceImpl helloService;

    @Autowired
    ThriftCodecManager thriftCodecManager;

    @Autowired
    TCalculatorService calculatorService;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("run TMultiplexedProcessor and TServer listening {}.....", ServiceConfigurationConstants.SERVICE_PORT);
        TServerTransport t = new TServerSocket(ServiceConfigurationConstants.SERVICE_PORT);
        TMultiplexedProcessor tMultiplexedProcessor = new TMultiplexedProcessor();
        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(t).processor(tMultiplexedProcessor));

        HelloServiceImpl helloService = new HelloServiceImpl();
        Hello.Processor helloProcessor = new Hello.Processor(helloService);

        CalculatorImpl calculatorService = new CalculatorImpl();
        Calculator.Processor calculatorProcessor = new Calculator.Processor(calculatorService);

        // serviceName Name of a service, has to be identical to the name declared in the Thrift IDL, e.g. "Hello".
        tMultiplexedProcessor.registerProcessor(Hello.class.getSimpleName(), helloProcessor);
        tMultiplexedProcessor.registerProcessor(Calculator.class.getSimpleName(), calculatorProcessor);

        Thread services = new Thread(() -> server.serve());
        services.setDaemon(true);
        services.start();
    }

}
