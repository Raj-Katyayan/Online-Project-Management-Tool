package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskAssignmentDAO {

    public boolean isAssigned(int taskId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM task_assignments WHERE task_id = ? AND user_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean assignTask(int taskId, int userId) throws SQLException {
        if (isAssigned(taskId, userId)) {
            return false;
        }

        String sql = "INSERT INTO task_assignments (task_id, user_id) VALUES (?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean unassignTask(int taskId, int userId) throws SQLException {
        String sql = "DELETE FROM task_assignments WHERE task_id = ? AND user_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    public List<Integer> getAssigneeIds(int taskId) throws SQLException {
        String sql = "SELECT user_id FROM task_assignments WHERE task_id = ?";

        List<Integer> ids = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("user_id"));
                }
            }
        }

        return ids;
    }
}