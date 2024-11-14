package com.example.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resourceserver01")
public class IndexController {

    @GetMapping
    public String index(){
        return "Hello World From Resource Server 01";
    }
}
