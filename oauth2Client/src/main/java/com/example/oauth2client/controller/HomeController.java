package com.example.oauth2client.controller;

import com.example.oauth2client.config.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final OAuth2Properties oAuth2Properties;
    @Value("${spring.security.oauth2.client.registration.youtube-lecture-oauth2.client-id}")
    String client_id;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String Home(Model model, Authentication authentication) {
        model.addAttribute("login", authentication);
        model.addAttribute("loginUrl", oAuth2Properties.getLoginUrl());
        return "index";
    }

    @GetMapping("/logged-out")
    public String logout(Model model, Authentication authentication) {
        model.addAttribute("logout", authentication);
        return "index";
    }
}
