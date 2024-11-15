package com.example.authorizationserver.rotate.repository;

import com.example.authorizationserver.rotate.key.RsaKeyPair;
import com.example.authorizationserver.rotate.repository.serialize.RsaPublicKeyConverter;
import com.example.authorizationserver.rotate.repository.serialize.RsaPublicKeyConverter.RsaPrivateKeyConverter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class JdbcRsaKeyPairRepository implements RsaKeyPairRepository {

    private final JdbcClient jdbcClient;
    // @see RsaKeyPairRowMapper
    private final RowMapper<RsaKeyPair> keyPairRowMapper;

    private final RsaPublicKeyConverter rsaPublicKeyConverter;
    private final RsaPrivateKeyConverter rsaPrivateKeyConverter;

    public JdbcRsaKeyPairRepository(JdbcClient jdbcClient, RowMapper<RsaKeyPair> keyPairRowMapper
            , RsaPublicKeyConverter rsaPublicKeyConverter, RsaPrivateKeyConverter rsaPrivateKeyConverter
    ) {
        this.jdbcClient = jdbcClient;
        this.keyPairRowMapper = keyPairRowMapper;
        this.rsaPublicKeyConverter = rsaPublicKeyConverter;
        this.rsaPrivateKeyConverter = rsaPrivateKeyConverter;
    }


    @Override
    public void save(RsaKeyPair rsaKeyPair) {
        String sql = "insert into rsa_key_pairs (id, created, public_key, private_key ) values (?, ?, ?, ?)";
        try (ByteArrayOutputStream privateBaos = new ByteArrayOutputStream(); var publicBaos = new ByteArrayOutputStream()) {
            this.rsaPrivateKeyConverter.serialize(rsaKeyPair.privateKey(), privateBaos);
            this.rsaPublicKeyConverter.serialize(rsaKeyPair.publicKey(), publicBaos);

            this.jdbcClient.sql(sql)
                    .params(
                            rsaKeyPair.id(),
                            new Date(rsaKeyPair.created().toEpochMilli()),
                            publicBaos.toString(),
                            privateBaos.toString())
                    .update();
            return;
        } catch (IOException e) {
            throw new IllegalArgumentException("there's been an exception", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM rsa_key_pairs WHERE id = :id";
        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    @Override
    /* find latest key */
    public List<RsaKeyPair> findKeyPair() {
        String sql = "select * from rsa_key_pairs order by created desc";
        return jdbcClient.sql(sql)
                .query(this.keyPairRowMapper)
                .list();
    }
}
