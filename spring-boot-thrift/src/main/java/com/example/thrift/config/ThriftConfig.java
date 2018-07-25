package com.example.thrift.config;

import com.example.thrift.protocol.TCalculatorService;
import com.facebook.nifty.processor.NiftyProcessorAdapters;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServiceProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author yuweijun 2016-06-13
 */
@Configuration
public class ThriftConfig {

    @Bean
    public TProtocolFactory tProtocolFactory() {
        // We will use binary protocol, but it's possible to use JSON and few others as well
        return new TBinaryProtocol.Factory();
    }

    @Bean
    public ThriftCodecManager thriftCodecManager() {
        return new ThriftCodecManager();
    }

    @Bean
    public ServletRegistrationBean calculator(ThriftCodecManager thriftCodecManager, TProtocolFactory tprotocolFactory, TCalculatorService calculatorService) {
        ThriftServiceProcessor processor = new ThriftServiceProcessor(thriftCodecManager, Arrays.<ThriftEventHandler>asList(), calculatorService);
        TProcessor tProcessor = NiftyProcessorAdapters.processorToTProcessor(processor);
        TServlet tServlet = new TServlet(tProcessor, tprotocolFactory, tprotocolFactory);
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(tServlet, "/calculator/");
        return registrationBean;
    }

}
