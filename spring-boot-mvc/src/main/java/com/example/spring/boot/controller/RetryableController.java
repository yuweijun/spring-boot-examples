package com.example.spring.boot.controller;

import com.example.spring.boot.service.RetryableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuweijun
 * @since 2018-11-19
 */
@RestController
public class RetryableController {

    @Autowired
    private RetryableService retryableService;

    @GetMapping("/retry/get")
    public Map<String, Object> get(@RequestParam("arg") String arg) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", new Date());

        retryableService.call(arg);
        return map;
    }

}
