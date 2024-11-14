package com.example.gateway.entity;

public record AuthorizationConsent(
        String registeredClientId,
        String principalName,
        String authorities
) {
}