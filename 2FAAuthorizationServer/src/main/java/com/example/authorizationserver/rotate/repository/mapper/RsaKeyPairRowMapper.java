package com.example.authorizationserver.rotate.repository.mapper;

import com.example.authorizationserver.rotate.key.RsaKeyPair;
import com.example.authorizationserver.rotate.repository.serialize.RsaPublicKeyConverter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

@Component
public class RsaKeyPairRowMapper implements RowMapper<RsaKeyPair> {

    private final RsaPublicKeyConverter.RsaPrivateKeyConverter privateKeyConverter;
    private final RsaPublicKeyConverter publicKeyConverter;

    public RsaKeyPairRowMapper(RsaPublicKeyConverter.RsaPrivateKeyConverter privateKeyConverter, RsaPublicKeyConverter publicKeyConverter) {
        this.privateKeyConverter = privateKeyConverter; //TextEncryptor
        this.publicKeyConverter = publicKeyConverter;
    }

    @Override
    public RsaKeyPair mapRow(ResultSet rs, int rowNum) throws SQLException {

        byte[] privateKeyBytes = rs.getString("private_key").getBytes();
        byte[] publicKeyBytes = rs.getString("public_key").getBytes();

        // deserialize
        RSAPrivateKey privateKey = null;
        RSAPublicKey publicKey = null;
        try {
            privateKey = privateKeyConverter.deserializeFromByteArray(privateKeyBytes);
            publicKey = publicKeyConverter.deserializeFromByteArray(publicKeyBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Instant created = new Date(rs.getDate("created").getTime()).toInstant();
        return new RsaKeyPair(
                rs.getString("id"),
                created,
                publicKey,
                privateKey
        );
    }

}
