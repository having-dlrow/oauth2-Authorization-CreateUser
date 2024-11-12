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

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/user_rowmapper")
    public List<User> getAllUsersRowMapper() {
        return userService.findAllUsersRowMapper();
    }

    @GetMapping("/user_test")
    public List<UserTest> getAllUsersTest() {
        return userService.findAllUsersTest();
    }

    @GetMapping("/user_test_rowmapper")
    public List<UserTest> getAllUsersTestRowMapper() {
        return userService.findAllUsersTestRowMapper();
    }

    @GetMapping("/users_authorities")
    public List<User_Authority> getAllUsersAuthorities() {
        return userService.findAllUsersAuthorities();
    }

    @GetMapping("/authorities_users")
    public List<Authorities> getAllAuthoritiesUsers() {
        return userService.findAllAuthoritiesUsers();
    }

    @GetMapping("/user/{username}")
    public Optional<User> findByUserName(@PathVariable(name = "username") String username) {
        return userService.findByUsername(username);
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
    public void deleteUser(@PathVariable(name = "username") String username) {
        userService.deleteUser(username);
    }
}