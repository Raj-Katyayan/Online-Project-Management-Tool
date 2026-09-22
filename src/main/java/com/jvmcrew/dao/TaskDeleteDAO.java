package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDeleteDAO {

    public String deleteTask(
            int taskId,
            int projectId,
            int managerId) throws SQLException {

        String ownershipSql =
                "SELECT t.title " +
                "FROM tasks t " +
                "JOIN projects p ON t.project_id = p.project_id " +
                "WHERE t.task_id = ? " +
                "AND t.project_id = ? " +
                "AND p.manager_id = ?";

        String taskTitle;

        String deleteAssignmentsSql =
                "DELETE FROM task_assignments WHERE task_id = ?";

        String deleteTaskSql =
                "DELETE FROM tasks " +
                "WHERE task_id = ? AND project_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                /*
                 * SECURITY CHECK
                 *
                 * The manager must own the project
                 * containing the task.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     ownershipSql)) {

                    ps.setInt(1, taskId);
                    ps.setInt(2, projectId);
                    ps.setInt(3, managerId);

                    try (ResultSet rs =
                                 ps.executeQuery()) {

                        if (!rs.next()) {

                            connection.rollback();

                            return null;
                        }

                        taskTitle =
                                rs.getString("title");
                    }
                }


                /*
                 * Remove all assignments first.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteAssignmentsSql)) {

                    ps.setInt(1, taskId);

                    ps.executeUpdate();
                }


                /*
                 * Delete the task.
                 */
                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     deleteTaskSql)) {

                    ps.setInt(1, taskId);
                    ps.setInt(2, projectId);

                    int deleted =
                            ps.executeUpdate();

                    if (deleted != 1) {

                        connection.rollback();

                        return null;
                    }
                }


                connection.commit();

                return taskTitle;

            } catch (SQLException e) {

                connection.rollback();

                throw e;

            } finally {

                connection.setAutoCommit(true);
            }
        }
    }
}