package com.example.gateway.controller;

import com.example.gateway.model.Authority;
import com.example.gateway.service.AuthorityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authority")
    public List<Authority> getAllAuthorities() {
        return authorityService.findAllAuthorities();
    }
}
