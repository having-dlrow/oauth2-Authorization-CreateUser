package com.example.oauth2client.service;

import com.example.oauth2client.model.Authority;
import com.example.oauth2client.model.User;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface UserClient {

    @GetExchange("/user")
    List<User> getUsers();

    @GetExchange("/authority")
    List<Authority> getAuthorities();
}
