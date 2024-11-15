package com.example.authorizationserver.rotate;

import com.example.authorizationserver.rotate.key.Keys;
import com.example.authorizationserver.rotate.key.RsaKeyPair;
import com.example.authorizationserver.rotate.repository.RsaKeyPairRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class KeyGenerateRunner implements ApplicationRunner {

    private final RsaKeyPairRepository repository;
    private final Keys keys;

    public KeyGenerateRunner(RsaKeyPairRepository repository, Keys keys) {
        this.repository = repository;
        this.keys = keys;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (this.repository.findKeyPair().isEmpty()) {
            //generate
            RsaKeyPair keypair = keys.generateKeyPair(Instant.now());
            this.repository.save(keypair);
        }
    }
}
