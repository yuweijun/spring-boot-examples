package com.example.spring.boot;

import org.junit.Test;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Utility class for URI encoding and decoding based on RFC 3986. Offers encoding methods for the various URI components.
 */
public class UriUtilsTest {

    @Test
    public void testCase1() throws UnsupportedEncodingException {
        String encode1 = UriUtils.encode("+|`^!'()*~-_.09azAZ", "UTF-8");
        System.out.println(encode1);

        String decode1 = UriUtils.decode("+|`^!'()*~-_.09azAZ", "UTF-8");
        System.out.println(decode1);

        String encode2 = URLEncoder.encode("+|`^!'()*~-_.09azAZ", "UTF-8");
        System.out.println(encode2);

        String decode2 = URLDecoder.decode("%2B%7C%60%5E%21%27%28%29*%7E-_.09azAZ", "UTF-8");
        System.out.println(decode2);
    }

    @Test
    public void testCase3() throws UnsupportedEncodingException {
        String encode1 = UriUtils.encode(" |`^!'()*~-_.09azAZ", "UTF-8");
        String encode2 = URLEncoder.encode(" |`^!'()*~-_.09azAZ", "UTF-8");
        System.out.println(encode1);
        System.out.println(encode2);

        String decode1 = UriUtils.decode("%20%7C%60%5E%21%27%28%29*%7E-_.09azAZ", "UTF-8");
        String decode2 = URLDecoder.decode("+%7C%60%5E%21%27%28%29*%7E-_.09azAZ", "UTF-8");
        System.out.println(decode1);
        System.out.println(decode2);
    }

    @Test
    public void testCase5() throws UnsupportedEncodingException {
    }
}
