package com.example.gateway.entity;

import java.util.List;

public record User(String username,
                   String password,
                   Boolean enabled,
                   List<String> authority) {

    public void addAuthority(String authority) {
        this.authority.add(authority);
    }

}
