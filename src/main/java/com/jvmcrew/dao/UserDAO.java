package com.jvmcrew.dao;

import com.jvmcrew.model.User;
import com.jvmcrew.util.DBConnection;
import com.jvmcrew.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, PasswordUtil.hashPassword(user.getPassword()));
            ps.setString(4, user.getRole());
            return ps.executeUpdate() > 0;
        }
    }

    public User loginUser(String email, String password) throws SQLException {
        String sql = "SELECT user_id, name, email, password, role FROM users WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String stored = rs.getString("password");
                if (!PasswordUtil.verifyPassword(password, stored)) {
                    return null;
                }
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }
    }

    public List<User> getAllMembers() throws SQLException {
        return getUsersByRole("MEMBER");
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT user_id, name, email, role FROM users ORDER BY user_id";
        List<User> users = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                users.add(u);
            }
        }
        return users;
    }

    private List<User> getUsersByRole(String role) throws SQLException {
        String sql = "SELECT user_id, name, email, role FROM users WHERE role = ? ORDER BY name";
        List<User> users = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getString("role"));
                    users.add(u);
                }
            }
        }
        return users;
    }

    public boolean updateRole(int userId, String role) throws SQLException {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
