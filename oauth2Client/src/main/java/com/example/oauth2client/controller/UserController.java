package com.example.oauth2client.controller;

import com.example.oauth2client.model.User;
import com.example.oauth2client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUsers(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("uses", users);
        // view
        return "user";
    }
}