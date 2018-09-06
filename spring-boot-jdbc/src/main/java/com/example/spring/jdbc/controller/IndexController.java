package com.example.spring.jdbc.controller;

import com.example.spring.jdbc.service.DistributedPrimaryKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yuweijun 2017-04-23.
 */
@Controller
public class IndexController {

    @Autowired
    private DistributedPrimaryKeyService distributedPrimaryKeyService;

    @RequestMapping("/")
    public String index() {
        return "redirect:people/all";
    }

    @GetMapping("/distributed/primary/key")
    @ResponseBody
    public long id() {
        return distributedPrimaryKeyService.getDistributedUniquePrimaryKey();
    }

}
