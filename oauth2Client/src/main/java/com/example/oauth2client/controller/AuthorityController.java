package com.example.oauth2client.controller;

import com.example.oauth2client.model.Authority;
import com.example.oauth2client.service.AuthorityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/authority")
    public String getAuthorities(Model model) {
        List<Authority> authority = authorityService.getAuthorities();
        model.addAttribute("authorities", authority);
        return "authority";
    }
}
