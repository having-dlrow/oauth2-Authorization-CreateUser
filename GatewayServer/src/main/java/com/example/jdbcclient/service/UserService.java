package com.example.jdbcclient.service;

import java.util.List;

import com.example.jdbcclient.mapper.*;
import com.example.jdbcclient.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final String SQL_FIND_ALL_USERS = """
            SELECT users.username, users.password, users.enabled, authorities.authority 
            FROM users 
            LEFT JOIN authorities ON users.username = authorities.username ;
            """;

    private static final String SQL_FIND_USER_BY_USERNAME = """
            SELECT users.username, users.password, users.enabled, authorities.authority 
            FROM users 
            LEFT JOIN authorities ON users.username = authorities.username 
            WHERE users.username = ?;
            """;

    private static final String SQL_CREATE_USER = """
            INSERT INTO users(username, password, enabled)
            VALUES(?, ?, ?);
            """;

    private static final String SQL_CREATE_AUTHORITY = """
            INSERT INTO authorities(username, authority)
            VALUES(?, ?);
            """;

    private static final String SQL_UPDATE_USER = """
            UPDATE users SET password = ?, enabled = ? WHERE username = ?;
            """;

    private static final String SQL_DELETE_USER = """
            DELETE FROM users WHERE users.username = ?;
            """;

    private static final String SQL_DELETE_AUTHORITY = """
            DELETE FROM authorities WHERE authorities.username = ?
            """;

    private static final String SQL_EXISTS_USERNAME = """
            SELECT count(*) FROM users WHERE users.username = ?;
            """;

    private final JdbcClient jdbcClient;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return jdbcClient.sql(SQL_FIND_ALL_USERS)
                .query(new UserExtrator()); // create Authority as List
    }

    public User findByUsername(String username) {
        return jdbcClient.sql(SQL_FIND_USER_BY_USERNAME)
                .param(username)
                .query(new UserByUsernameExtractor());
    }

    public boolean isExistsUser(String username) {
        return jdbcClient.sql(SQL_EXISTS_USERNAME)
                .param(username)
                .query(Integer.class).single() > 0;
    }

    private String encode(String password) {
        return "{bcrypt}" + passwordEncoder.encode(password);
    }

    public Integer createUser(User user) {

        log.debug(user.toString());
        if (isExistsUser(user.username())) {
            return 0;
        }

        int rows = jdbcClient.sql(SQL_CREATE_USER)
                .params(user.username(), encode(user.password()), user.enabled())
                .update();

        createUserAuthority(user);
        return rows;
    }

    private void createUserAuthority(User user) {

        log.debug(user.toString());
        log.debug(user.authority().isEmpty() + "");
        if (user.authority().isEmpty()) {
            log.debug(user.authority().toString());
            return;
        }

        // list
        String username = user.username();
        user.authority().forEach(authority -> {
            log.debug(authority);
            log.debug(user.username());
            jdbcClient.sql(SQL_CREATE_AUTHORITY)
                    .params(username, authority)
                    .update();
        });
    }

    public Integer updateUser(User user) {
        if (isExistsUser(user.username())) {
            Integer updated = jdbcClient.sql(SQL_UPDATE_USER)
                    .params(user.password(), user.enabled(), user.username())
                    .update();
            if (updated == 0) {
                throw new RuntimeException("User not found");
            }
            return updated;
        }
        return 0;
    }

    public Integer deleteUser(String username) {
        if (isExistsUser(username)) {
            deleteUserCascade(username);
            deleteUserAuthorityCascade(username);
        }
        return 0;
    }

    private void deleteUserAuthorityCascade(String username) {
        Integer updated = jdbcClient.sql(SQL_DELETE_AUTHORITY)
                .params(username)
                .update();
        if (updated == 0) {
            throw new RuntimeException("User not found");
        }
    }

    private void deleteUserCascade(String username) {
        Integer updated = jdbcClient.sql(SQL_DELETE_USER)
                .params(username)
                .update();
        if (updated == 0) {
            throw new RuntimeException("User not found");
        }
    }
}
