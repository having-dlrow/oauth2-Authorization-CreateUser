package com.example.gateway.controller;

import com.example.gateway.entity.Authorization;
import com.example.gateway.service.db.AuthorizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resourceserver03")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/authorization")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Authorization> findAllSession() {
        return authorizationService.findAllAuthorization();
    }
}
