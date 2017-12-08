package com.example.spring.controller;

import com.example.spring.util.TrustStoreClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuweijun 2017-05-28.
 */
@RestController
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    public String test() throws Exception {
        TrustStoreClient.init();

        return "test";
    }

}
