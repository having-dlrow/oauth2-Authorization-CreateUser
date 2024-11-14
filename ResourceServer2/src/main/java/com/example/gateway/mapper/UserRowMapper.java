package com.example.gateway.mapper;

import com.example.gateway.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("enabled"),
                new ArrayList<>()
        );
    }
}