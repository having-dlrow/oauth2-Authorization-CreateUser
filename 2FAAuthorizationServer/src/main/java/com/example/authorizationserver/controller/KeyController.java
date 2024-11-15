package com.example.authorizationserver.controller;

import com.example.authorizationserver.rotate.key.Keys;
import com.example.authorizationserver.rotate.key.RsaKeyPair;
import com.example.authorizationserver.rotate.repository.RsaKeyPairRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class KeyController {

    private final RsaKeyPairRepository repository;
    private final Keys keys;

    public KeyController(RsaKeyPairRepository repository, Keys keys) {
        this.repository = repository;
        this.keys = keys;
    }

    @GetMapping("/oauth2/new_jwks")
    public String generate() {
        RsaKeyPair keyPair = keys.generateKeyPair(Instant.now());
        repository.save(keyPair);
        return keyPair.id();
    }
}
