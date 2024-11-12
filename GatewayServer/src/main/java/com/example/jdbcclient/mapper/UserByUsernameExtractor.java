package com.example.jdbcclient.mapper;

import com.example.jdbcclient.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserByUsernameExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        User user = null;
        while (rs.next()) {
            String username = rs.getString("username");
            if (rs.isFirst()) {
                user = new User(
                        username,
                        rs.getString("password"),
                        rs.getBoolean("enabled"),
                        new ArrayList<>()
                );
                user.authority().add(rs.getString("authority"));
            }
        }
        return user;
    }
}
