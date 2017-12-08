package com.example.controller;

import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * https://stackoverflow.com/questions/1548782/retrieving-json-object-literal-from-httpservletrequest
 * <p>
 * $ curl -X POST --header 'Content-Type: application/json' -d '{ "departmentName": "tec", "id": 1, "loginName": "yu" }' 'http://localhost:8080/post/json/reader'
 * $ curl -X POST --header 'Content-Type: application/json' -d '{ "departmentName": "tec", "id": 1, "loginName": "yu" }' 'http://localhost:8080/post/json/gson'
 * <p>
 * 因为@RestController只接受json类的请求，所以上面必须加上--header 'Content-Type: application/json'这个头部
 * <p>
 * 另外在MvcConfiguration中设置http.csrf().ignoringAntMatchers("/post/json");跳过csrf验证，不然抛类似下面的错误：
 * {"status":403,"error":"Forbidden","message":"Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.","timeStamp":"Thu Oct 19 09:07:49 CST 2017","trace":null}
 * Created by yuweijun on 2017-10-18.
 */
@RestController
@RequestMapping("/post/json")
public class PostJsonBodyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostJsonBodyController.class);

    @RequestMapping(value = "/reader", method = RequestMethod.POST)
    public String readerPostBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        LOGGER.info("reader : {}", sb);
        return sb.toString();
    }

    @RequestMapping(value = "/gson", method = RequestMethod.POST)
    public User readByGson(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();

        User user = gson.fromJson(reader, User.class);
        LOGGER.info("gson : {}", user);
        return user;
    }

    @RequestMapping(value = "/jackson", method = RequestMethod.POST)
    public User readByJackson(HttpServletRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(request.getInputStream(), User.class);
        LOGGER.info("jackson : {}", user);
        return user;
    }

    @RequestMapping(value = "/java8", method = RequestMethod.POST)
    public String java8Stream(HttpServletRequest request) throws IOException {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        LOGGER.info("java8 : {}", requestData);
        return requestData;
    }
}
