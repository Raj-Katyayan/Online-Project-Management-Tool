package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActivityLogDAO {

    public void log(
            int userId,
            Integer projectId,
            String action,
            String description) throws SQLException {

        String sql =
                "INSERT INTO activity_logs " +
                "(user_id, project_id, action, description) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);

            if (projectId == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, projectId);
            }

            ps.setString(3, action);
            ps.setString(4, description);

            ps.executeUpdate();
        }
    }


    public List<Map<String, Object>> getRecentLogs(int limit)
            throws SQLException {

        String sql =
                "SELECT " +
                "a.activity_id, " +
                "a.action, " +
                "a.description, " +
                "a.created_at, " +
                "u.name AS user_name, " +
                "p.project_name " +
                "FROM activity_logs a " +
                "LEFT JOIN users u ON a.user_id = u.user_id " +
                "LEFT JOIN projects p ON a.project_id = p.project_id " +
                "ORDER BY a.activity_id DESC " +
                "LIMIT ?";

        List<Map<String, Object>> logs = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> log =
                            new LinkedHashMap<>();

                    log.put(
                            "activityId",
                            rs.getInt("activity_id")
                    );

                    log.put(
                            "action",
                            rs.getString("action")
                    );

                    log.put(
                            "description",
                            rs.getString("description")
                    );

                    log.put(
                            "createdAt",
                            rs.getTimestamp("created_at")
                    );

                    log.put(
                            "userName",
                            rs.getString("user_name")
                    );

                    log.put(
                            "projectName",
                            rs.getString("project_name")
                    );

                    logs.add(log);
                }
            }
        }

        return logs;
    }
}