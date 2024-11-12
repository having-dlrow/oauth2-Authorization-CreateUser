package com.example.jdbcclient.model;

public record User(String username,
                   String password,
                   Boolean enabled) {

}
