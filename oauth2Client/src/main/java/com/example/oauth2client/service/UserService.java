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

    public User getUserByUsername(String username) {
        return this.userClient.getUserByUsername(username);
    }

    public Integer createUser(User user) {
        return this.userClient.createUser(user);
    }

    public Integer updateUser(User user) {
        return this.userClient.updateUser(user);
    }

    public Integer deleteUser(String username) {
        return this.userClient.deleteuser(username);
    }
}
