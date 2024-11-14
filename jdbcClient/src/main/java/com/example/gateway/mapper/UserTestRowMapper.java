package com.example.gateway.mapper;

import com.example.gateway.model.UserTest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTestRowMapper implements RowMapper<UserTest> {

    @Override
    public UserTest mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserTest(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"), rs.getString("test"));
    }

}