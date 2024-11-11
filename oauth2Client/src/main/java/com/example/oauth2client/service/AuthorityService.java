package com.example.oauth2client.service;

import com.example.oauth2client.model.Authority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    public final UserClient userClient;

    public AuthorityService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<Authority> getAuthorities() {
        return this.userClient.getAuthorities();
    }
}
