package com.example.gateway.controller;

import com.example.gateway.entity.Oauth2RegisteredClient;
import com.example.gateway.service.db.Oauth2RegisteredClientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/resourceserver03")
public class Oauth2RegisteredClientController {
    private final Oauth2RegisteredClientService oauth2RegisteredClientService;

    public Oauth2RegisteredClientController(Oauth2RegisteredClientService oauth2RegisteredClientService) {
        this.oauth2RegisteredClientService = oauth2RegisteredClientService;
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Oauth2RegisteredClient> findAllOauth2RegisteredClient() {
        return oauth2RegisteredClientService.findAllClients();
    }
}
