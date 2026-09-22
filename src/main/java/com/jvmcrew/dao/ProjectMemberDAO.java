package com.jvmcrew.dao;

import com.jvmcrew.model.User;
import com.jvmcrew.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDAO {

    public boolean isMember(
            int projectId,
            int userId) throws SQLException {

        String sql =
                "SELECT 1 FROM project_members " +
                "WHERE project_id = ? AND user_id = ?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }


    public boolean addMember(
            int projectId,
            int userId) throws SQLException {

        if (isMember(projectId, userId)) {
            return false;
        }

        String sql =
                "INSERT INTO project_members " +
                "(project_id, user_id) VALUES (?, ?)";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }


    public List<User> getMembersByProject(int projectId)
            throws SQLException {

        String sql = """
                SELECT u.user_id, u.name, u.email, u.role
                FROM users u
                JOIN project_members pm
                    ON u.user_id = pm.user_id
                WHERE pm.project_id = ?
                ORDER BY u.name
                """;

        List<User> members = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    User u = new User();

                    u.setUserId(
                            rs.getInt("user_id")
                    );

                    u.setName(
                            rs.getString("name")
                    );

                    u.setEmail(
                            rs.getString("email")
                    );

                    u.setRole(
                            rs.getString("role")
                    );

                    members.add(u);
                }
            }
        }

        return members;
    }


    public List<Integer> getMemberIds(int projectId)
            throws SQLException {

        String sql =
                "SELECT user_id " +
                "FROM project_members " +
                "WHERE project_id = ?";

        List<Integer> ids = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ids.add(
                            rs.getInt("user_id")
                    );
                }
            }
        }

        return ids;
    }


    /*
     * =========================
     * DASHBOARD STATISTICS
     * =========================
     */

    public int getUniqueMemberCountByManager(int managerId)
            throws SQLException {

        String sql = """
                SELECT COUNT(DISTINCT pm.user_id)
                FROM project_members pm
                JOIN projects p
                    ON pm.project_id = p.project_id
                WHERE p.manager_id = ?
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }
}
