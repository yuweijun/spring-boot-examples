package com.example.thrift.runner;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.Hello;
import com.example.thrift.hello.HelloServiceImpl;
import com.facebook.nifty.core.NettyServerTransport;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.nifty.core.ThriftServerDefBuilder;
import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2016-07-31.
 */
@Component
public class NiftyServerCommandLineRunner implements CommandLineRunner {

    private static final int PORT = ServiceConfigurationConstants.MAP_CONST.get("nifty");

    private static final Logger LOGGER = LoggerFactory.getLogger(NiftyServerCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        this.startServer();
    }

    private void startServer() {
        // Create the handler
        Hello.Iface serviceInterface = new HelloServiceImpl();

        // Create the processor
        TProcessor processor = new Hello.Processor<>(serviceInterface);

        // Build the server definition
        ThriftServerDef serverDef = new ThriftServerDefBuilder().withProcessor(processor)
                .listen(PORT)
                .build();

        // Create the server transport
        final NettyServerTransport server = new NettyServerTransport(serverDef);

        // Start the server
        server.start();
        LOGGER.info("Nifty server start as port : {}......", PORT);

        // Arrange to stop the server at shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    server.stop();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

}
