package com.example.spring.narayana.controller;

import com.example.spring.narayana.service.JdbcQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Weijun Yu
 * @since 2020-01-22
 */
@RestController
public class HomeController {

    @Autowired
    private JdbcQueryService jdbcQueryService;

    @RequestMapping("/")
    public Map<String, Object> index() {
        return jdbcQueryService.queryDatabases();
    }

}
