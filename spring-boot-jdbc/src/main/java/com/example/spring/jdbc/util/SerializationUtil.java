package com.example.spring.jdbc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author yuweijun 2016-09-20
 */
public class SerializationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtil.class);

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            LOGGER.error("serialize error.", e);
        } finally {
            closeQuietly(oos);
            closeQuietly(baos);
        }

        return null;
    }

    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            LOGGER.error("deserialize error", e);
        } finally {
            closeQuietly(bais);
        }

        return null;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("close error.", e);
            }
        }
    }

}
