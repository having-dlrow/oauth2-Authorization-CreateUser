package com.example.gateway.service;

import com.example.gateway.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final String SQL_FIND_ALL_ROLES = """
            SELECT roles.role 
            FROM roles ;
            """;

    private static final String SQL_CREATE_ROLE = """
            INSERT INTO roles(role)
            VALUES(?);
            """;

    private final JdbcClient jdbcClient;

    public List<Role> findAllRoles() {
        return jdbcClient.sql(SQL_FIND_ALL_ROLES)
                .query(Role.class)
                .list();
    }

    public Integer createRole(Role role) {

        int rows = jdbcClient.sql(SQL_CREATE_ROLE)
                .params(role.role())
                .update();

        return rows;
    }
}
