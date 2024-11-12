package com.example.oauth2client.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final UserClient userClient;

    public RoleService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        this.userClient.getRoles().forEach(role -> roles.add(role.role()));
        return roles;
    }
}
