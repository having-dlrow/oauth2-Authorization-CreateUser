package com.example.jdbcclient.model;

import java.util.List;

public record Authorities(
        String authority,
        List<UserTest> users) {
}