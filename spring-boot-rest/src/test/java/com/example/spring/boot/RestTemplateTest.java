package com.example.spring.boot;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by yuweijun on 2018-06-04.
 */
public class RestTemplateTest {

    @Test
    public void testCase1() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Map map = restTemplate.getForObject("https://httpbin.org/get", Map.class);
        System.out.println(map);
    }

    @Test
    public void testCase2() {
        final String uri = "https://httpbin.org/post?x=1&y=2";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("a=1&b=2&c=3", headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        System.out.println(result);
    }

    @Test
    public void testCase3() {
        RestTemplate restTemplate = new RestTemplate();
        String str = restTemplate.getForObject("https://httpbin.org/ip", String.class);
        System.out.println(str);
    }

    @Test
    public void testCase4() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");

        Map<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("id", new Random(100).nextInt() + "");
        hashMap.put("date", new Date().toString());
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(hashMap, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange("https://httpbin.org/delete", HttpMethod.DELETE, requestEntity, String.class);

        String body = resp.getBody();
        System.out.println(body);
    }

    @Test
    public void testCase5() throws URISyntaxException {
        MultiValueMap<String, Object> multiPartBody = new LinkedMultiValueMap<>();
        multiPartBody.add("file", new ClassPathResource("logback-test.xml"));
        RequestEntity<MultiValueMap<String, Object>> requestEntity = RequestEntity
                .post(new URI("https://httpbin.org/post"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multiPartBody);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(requestEntity, String.class);

        String body = resp.getBody();
        System.out.println(body);
    }

}
