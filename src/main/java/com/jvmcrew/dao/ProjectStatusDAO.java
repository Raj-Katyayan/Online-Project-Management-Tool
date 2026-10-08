package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProjectStatusDAO {

    /*
     * Updates the status of a project only when
     * the project belongs to the specified manager.
     *
     * This prevents a manager from modifying
     * another manager's project.
     */
    public boolean updateStatus(
            int projectId,
            int managerId,
            String status) throws SQLException {

        String sql = """
                UPDATE projects
                SET status = ?
                WHERE project_id = ?
                  AND manager_id = ?
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, projectId);
            ps.setInt(3, managerId);

            return ps.executeUpdate() > 0;
        }
    }
}