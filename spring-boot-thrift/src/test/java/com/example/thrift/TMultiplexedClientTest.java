package com.example.thrift;

import com.example.thrift.config.ServiceConfigurationConstants;
import com.example.thrift.hello.Hello;
import com.example.thrift.protocol.TDivisionByZeroException;
import com.example.thrift.tutorial.Calculator;
import com.example.thrift.tutorial.Operation;
import com.example.thrift.tutorial.Work;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author yuweijun 2016-06-14
 */
public class TMultiplexedClientTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void client() throws TException, ExecutionException, InterruptedException, TDivisionByZeroException {
        TSocket transport = new TSocket("localhost", ServiceConfigurationConstants.SERVICE_PORT);

        // 如果不打开连接的话, Thrift.Transport.TTransportException: Cannot write to null outputstream
        transport.open();

        // 复用transport和protocol, 这样一个socket打开后可以实现监听多个不同的服务, 客户端调用时需要指明调用的服务类型。
        TBinaryProtocol protocol = new TBinaryProtocol(transport);

        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, Hello.class.getSimpleName());
        Hello.Client client = new Hello.Client(multiplexedProtocol);
        String helloString = client.helloString("just a test!");
        logger.debug("helloString is ======== {}", helloString);

        logger.info("Hello.class.getName() {}", Hello.class.getName());
        logger.info("Hello.class.getCanonicalName() {}", Hello.class.getCanonicalName());
        logger.info("Hello.class.getSimpleName() {}", Hello.class.getSimpleName());

        TMultiplexedProtocol calculatorProtocol = new TMultiplexedProtocol(protocol, Calculator.class.getSimpleName());
        Calculator.Client calculator = new Calculator.Client(calculatorProtocol);
        logger.debug("add = {}", calculator.add(10, 2));
        calculator.zip();

        Work work = new Work();
        work.setNum1(10);
        work.setNum2(2);
        work.setOp(Operation.DIVIDE);
        logger.debug("calculate DIVIDE = {}", calculator.calculate(10, work));

        transport.close();
    }

}
