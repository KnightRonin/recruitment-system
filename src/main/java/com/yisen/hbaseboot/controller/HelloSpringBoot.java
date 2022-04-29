package com.yisen.hbaseboot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpringBoot {
    @RequestMapping("/hello")
    public String test(){
        return "Hello SpringBoot2";
    }
}
