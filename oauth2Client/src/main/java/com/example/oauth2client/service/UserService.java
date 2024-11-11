package com.example.oauth2client.service;

import com.example.oauth2client.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<User> getUsers() {
        return this.userClient.getUsers();
    }
}
