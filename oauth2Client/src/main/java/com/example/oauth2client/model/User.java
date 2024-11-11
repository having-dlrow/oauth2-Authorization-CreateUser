package com.example.oauth2client.model;

public record User(String username,
                   String password,
                   Boolean enabled) {

}
