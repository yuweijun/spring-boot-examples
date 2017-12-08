package com.example.thrift;

import com.example.thrift.hello.Hello;
import com.example.thrift.hello.HelloServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

public class HelloServiceServer {

	/**
	 * 启动 Thrift 服务器
	 */
	@Test
	public void server() {
		try {
			// 设置服务端口为 7911
			TServerSocket serverTransport = new TServerSocket(7911);
			// 设置协议工厂为 TBinaryProtocol.Factory
			TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
			// 关联处理器与 Hello 服务的实现
			TProcessor processor = new Hello.Processor(new HelloServiceImpl());

			TThreadPoolServer.Args helloprocessor = new TThreadPoolServer.Args(serverTransport).processor(processor);
			TServer server = new TThreadPoolServer(helloprocessor);
			System.out.println("Start server on port 7911...");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}

}