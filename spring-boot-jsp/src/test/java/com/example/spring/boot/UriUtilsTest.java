package com.example.spring.boot;

import org.junit.Test;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

/**
 * Utility class for URI encoding and decoding based on RFC 3986. Offers encoding methods for the various URI components.
 *
 * Created by yuweijun on 2018-04-20.
 */
public class UriUtilsTest {

    @Test
    public void testCase1() throws UnsupportedEncodingException {
        String encode = UriUtils.encode("Hello World-_.~", "UTF-8");
        System.out.println(encode);
    }
    
    @Test
    public void testCase2() throws UnsupportedEncodingException {
        String decode = UriUtils.decode("Hello%20World-_.~", "UTF-8");
        System.out.println(decode);
    }

    @Test
    public void testCase3() throws UnsupportedEncodingException {
        String encode = UriUtils.encode("Hello+World-_.~", "UTF-8");
        System.out.println(encode);
    }

    @Test
    public void testCase4() throws UnsupportedEncodingException {
        String decode = UriUtils.decode("Hello+World-_.~", "UTF-8");
        System.out.println(decode);
    }
}
