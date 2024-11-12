package com.example.jdbcclient.mapper;

import com.example.jdbcclient.model.User;
import com.example.jdbcclient.model.User_Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserExtrator implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

        // k:v
        Map<String, User> users = new LinkedHashMap<>();
        while (rs.next()) {
            String username = rs.getString("username");
            User user = users.get(username);
            if (user == null) {
                user = new User(
                        username,
                        rs.getString("password"),
                        rs.getBoolean("enabled"),
                        new ArrayList<>());
                users.put(username, user);
            }
            user.authority().add(rs.getString("authority"));
        }
        // list
        return new ArrayList<>(users.values());
    }
}
