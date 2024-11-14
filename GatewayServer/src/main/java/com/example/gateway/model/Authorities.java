package com.example.gateway.model;

import java.util.List;

public record Authorities(
        String authority,
        List<UserTest> users) {
}