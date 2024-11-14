package com.example.gateway.controller2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resourceserver02")
public class Resource2Controller {

    @GetMapping
    public String index(){
        return "Hello World From Resource Server 02";
    }
}
