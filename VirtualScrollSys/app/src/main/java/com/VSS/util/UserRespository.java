package com.VSS.util;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.VSS.model.User;

@Repository
public class UserRespository {

    private final JdbcTemplate jdbcTemplate;

    public UserRespository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // SQL query to retrieve all users
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM authapp_customuser";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    // SQL query to add a new user
    public int saveUser(User user) {
        String sql = "INSERT INTO authapp_customuser (fullname, username, role, email, phoneNumber, IDkey, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getFullname(), user.getUsername(), user.getRole(), user.getEmail(),
                user.getPhone(), user.getIDkey(), user.generateRandomPassword(10));
    }

    // SQL query to delete user by username
    public int deleteUserByUsername(String username) {
        String sql = "DELETE FROM authapp_customuser WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    public int banUserByUsername(String username) {
        String sql = "UPDATE authapp_customuser SET banned = true WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }
    
    public int unbanUserByUsername(String username) {
        String sql = "UPDATE authapp_customuser SET banned = false WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }
    

    

    // RowMapper to map database rows to User objects
    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFullname(rs.getString("fullname"));
        user.setUsername(rs.getString("username"));
        user.setRole(rs.getString("role"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phoneNumber"));
        user.setIDkey(rs.getString("idKey"));
        user.setBanned(rs.getBoolean("banned"));
        return user;
    };
}
