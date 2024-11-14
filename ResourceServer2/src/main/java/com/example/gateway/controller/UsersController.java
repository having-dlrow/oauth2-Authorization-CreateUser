package com.example.gateway.controller;

import com.example.gateway.entity.User;
import com.example.gateway.service.db.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resourceserver03")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<User> findAllUsers() {
        return usersService.findAllUsers();
    }
}
