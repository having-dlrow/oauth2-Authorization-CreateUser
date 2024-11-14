package com.example.authorizationserver.rotate.key;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

public record RsaKeyPair(
        String id,
        Instant created,
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey
) {
}
