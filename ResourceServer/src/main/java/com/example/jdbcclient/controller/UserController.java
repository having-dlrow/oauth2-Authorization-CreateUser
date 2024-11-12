package com.example.jdbcclient.controller;

import java.util.List;
import java.util.Optional;

import com.example.jdbcclient.model.Authorities;
import com.example.jdbcclient.model.User;
import com.example.jdbcclient.model.UserTest;
import com.example.jdbcclient.model.User_Authority;
import com.example.jdbcclient.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List
    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // GET
    @GetMapping("/user/{username}")
    public User findByUserName(@PathVariable(name = "username") String username) {
        return userService.findByUsername(username);
    }

    // CREATE
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // UPDATE
    @PutMapping("/user")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    // DELETE
    @DeleteMapping("/user/{username}")
    public void deleteUser(@PathVariable(name = "username") String username) {
        userService.deleteUser(username);
    }
}