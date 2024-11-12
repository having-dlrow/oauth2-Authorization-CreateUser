package com.example.jdbcclient.mapper;

import com.example.jdbcclient.model.User_Authority;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserResultExtractor implements ResultSetExtractor<List<User_Authority>> {

    @Override
    public List<User_Authority> extractData(ResultSet rs) throws SQLException, DataAccessException {

        // k:v
        Map<String, User_Authority> users = new LinkedHashMap<>();
        while (rs.next()) {
            String username = rs.getString("username");
            User_Authority user = users.get(username);
            if (user == null) {
                user = createUserAuthority(rs);
                users.put(username, user);
            }
            // MAP 중복허용안함
            user.authority().add(rs.getString("authority"));
        }
        // list<object>
        return new ArrayList<>(users.values());
    }

    private User_Authority createUserAuthority(ResultSet rs) throws SQLException {

        return new User_Authority(
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("enabled"),
                rs.getString("test"),
                new ArrayList<>());
    }
}