package com.example.thrift.provider;

import com.example.thrift.hello.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author yuweijun 2016-07-31.
 */
public class SimpleRpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRpcClient.class);

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException {
        //接口名称
        String interfaceName = Hello.Iface.class.getName();
        //需要执行的远程方法
        Method method = Hello.Iface.class.getMethod("helloString", java.lang.String.class);
        //需要传递到远程端的参数
        Object[] params = {"Hello World"};
        Socket socket = new Socket("127.0.0.1", 8888);

        //将方法名称和参数传递到远端
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeUTF(interfaceName);//接口名称
        outputStream.writeUTF(method.getName());//方法名称
        outputStream.writeObject(method.getParameterTypes());
        outputStream.writeObject(params);
        //从远端读取方法的执行结果
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        Object result = inputStream.readObject();
        LOGGER.info("Consumer result : {}", result);
    }

}
