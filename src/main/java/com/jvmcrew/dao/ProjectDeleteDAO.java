package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDeleteDAO {

    public String deleteProject(
            int projectId,
            int managerId) throws SQLException {

        String ownershipSql =
                "SELECT project_name " +
                "FROM projects " +
                "WHERE project_id = ? " +
                "AND manager_id = ?";

        String deleteAssignmentsSql =
                "DELETE ta " +
                "FROM task_assignments ta " +
                "JOIN tasks t ON ta.task_id = t.task_id " +
                "WHERE t.project_id = ?";

        String deleteTasksSql =
                "DELETE FROM tasks WHERE project_id = ?";

        String deleteMembersSql =
                "DELETE FROM project_members WHERE project_id = ?";

        String deleteActivitySql =
                "DELETE FROM activity_logs WHERE project_id = ?";

        String deleteProjectSql =
                "DELETE FROM projects " +
                "WHERE project_id = ? " +
                "AND manager_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                String projectName;

                /*
                 * SECURITY CHECK
                 *
                 * Confirm that the project belongs
                 * to the logged-in manager.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     ownershipSql)) {

                    ps.setInt(1, projectId);
                    ps.setInt(2, managerId);

                    try (ResultSet rs =
                                 ps.executeQuery()) {

                        if (!rs.next()) {

                            connection.rollback();

                            return null;
                        }

                        projectName =
                                rs.getString("project_name");
                    }
                }


                /*
                 * Remove task assignments first.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteAssignmentsSql)) {

                    ps.setInt(1, projectId);
                    ps.executeUpdate();
                }


                /*
                 * Remove tasks.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteTasksSql)) {

                    ps.setInt(1, projectId);
                    ps.executeUpdate();
                }


                /*
                 * Remove project members.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteMembersSql)) {

                    ps.setInt(1, projectId);
                    ps.executeUpdate();
                }


                /*
                 * Remove old activity entries
                 * attached to this project.
                 *
                 * A new PROJECT_DELETED entry will
                 * be added after the project is gone.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteActivitySql)) {

                    ps.setInt(1, projectId);
                    ps.executeUpdate();
                }


                /*
                 * Finally delete the project.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteProjectSql)) {

                    ps.setInt(1, projectId);
                    ps.setInt(2, managerId);

                    int deleted =
                            ps.executeUpdate();

                    if (deleted != 1) {

                        connection.rollback();

                        return null;
                    }
                }


                connection.commit();

                return projectName;

            } catch (SQLException e) {

                connection.rollback();

                throw e;

            } finally {

                connection.setAutoCommit(true);
            }
        }
    }
}