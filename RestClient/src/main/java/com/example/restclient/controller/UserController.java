package com.example.restclient.controller;

import com.example.restclient.service.User;
import com.example.restclient.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user/{username}")
    public User findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping("/user/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
