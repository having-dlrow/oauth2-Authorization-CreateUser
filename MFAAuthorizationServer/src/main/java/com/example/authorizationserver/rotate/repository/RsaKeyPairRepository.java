package com.example.authorizationserver.rotate.repository;

import com.example.authorizationserver.rotate.key.RsaKeyPair;

import java.util.List;

public interface RsaKeyPairRepository {

    void delete(String name);
    void save(RsaKeyPair rsaKeyPair);
    List<RsaKeyPair> findKeyPair();
}
