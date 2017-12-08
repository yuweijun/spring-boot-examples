package com.example.thrift.hello;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements Hello.Iface {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceImpl.class);

	@Override
	public boolean helloBoolean(boolean para) throws TException {
		return para;
	}

	@Override
	public int helloInt(int para) throws TException {
		try {
			LOGGER.info("Thread.sleep(2000) in HelloServiceImpl.class");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return para;
	}

	@Override
	public String helloNull() throws TException {
		// 客户端调用此方法会抛出异常, 不要在服务器端返回null给客户端
		// org.apache.thrift.TApplicationException: helloNull failed: unknown result
		return null;
	}

	@Override
	public String helloString(String para) throws TException {
		return para;
	}

	@Override
	public void helloVoid() throws TException {
		LOGGER.info("Hello World");
	}
}