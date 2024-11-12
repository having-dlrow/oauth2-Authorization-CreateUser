package com.example.jdbcclient.service;

import com.example.jdbcclient.model.Authority;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    private static final String SQL_FIND_ALL_AUTHORITY = """
			SELECT authorities.username, authorities.authority FROM authorities;
			""";

    private final JdbcClient jdbcClient;

    public AuthorityService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Authority> findAllAuthorities() {
        return jdbcClient.sql(SQL_FIND_ALL_AUTHORITY)
                .query(Authority.class)
                .list();
    }
}
