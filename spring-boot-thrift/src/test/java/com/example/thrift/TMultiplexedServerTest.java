package com.example.thrift;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.Hello;
import com.example.thrift.hello.HelloServiceImpl;
import com.example.thrift.tutorial.Calculator;
import com.example.thrift.tutorial.CalculatorImpl;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuweijun 2016-06-14
 */
public class TMultiplexedServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TMultiplexedServerTest.class);

    public static void main(String[] args) throws TTransportException {
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

        server.serve();
    }

}
