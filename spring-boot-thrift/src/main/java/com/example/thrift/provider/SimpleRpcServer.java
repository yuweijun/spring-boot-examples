package com.example.thrift.provider;

import com.example.thrift.hello.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author yuweijun 2016-07-31.
 */
public class SimpleRpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRpcServer.class);

    private static final int PORT = 8888;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        LOGGER.info("server start : {}", PORT);

        while (true) {
            try {
                Socket socket = server.accept();
                new RpcWork(socket).start();
            } catch (IOException e) {
                LOGGER.debug("{}", e.getMessage(), e);
            }
        }
    }

    private static class RpcWork extends Thread {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        public RpcWork(Socket socket) throws IOException {
            try {
                // 读取服务信息
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String interfaceName = inputStream.readUTF();//接口名称
                LOGGER.info("interfaceName : {}", interfaceName);
                String methodName = inputStream.readUTF();//方法名称
                LOGGER.info("methodName is {}", methodName);

                Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();//参数类型
                Object[] parameters = (Object[]) inputStream.readObject();//参数对象
                Class serviceInterfaceClass = Class.forName(interfaceName);
                // Object service = services.get(interfaceName);
                // 服务端将事先实例化好的服务放在services这个Map中，通过interfaceName取出使用
                Object service = new HelloServiceImpl(); //演示代码直接new了一个对象来使用
                // 获取要调用的方法
                Method method = serviceInterfaceClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service, parameters);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                // 将执行结果返回给调用端
                outputStream.writeObject(result);
                logger.info("Parameters from comsumer : {}", parameters[0]);
            } catch (Exception e) {
                LOGGER.debug("{}", e.getMessage(), e);
            } finally {
                socket.close();
            }
        }

    }

}
