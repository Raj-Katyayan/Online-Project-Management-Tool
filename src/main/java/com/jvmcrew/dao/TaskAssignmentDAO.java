package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access operations for task assignments.
 *
 * Assignment is performed with one INSERT ... SELECT statement. The SQL
 * statement itself verifies that the task belongs to the selected project,
 * that the project belongs to the manager, and that the selected user is a
 * member of that project with the MEMBER role. The database composite primary
 * key prevents duplicate rows.
 */
public class TaskAssignmentDAO {

    public enum AssignmentResult {
        ASSIGNED,
        ALREADY_ASSIGNED,
        INVALID_TASK_OR_MEMBER
    }

    /**
     * Assign a task only when all project, task, manager and membership
     * relationships are valid.
     *
     * @param projectId project selected in the manager dashboard
     * @param taskId task selected for assignment
     * @param userId project member to receive the task
     * @param managerId authenticated manager's user ID
     * @return the outcome of the assignment attempt
     * @throws SQLException when the database operation fails
     */
    public AssignmentResult assignTaskToProjectMember(
            int projectId,
            int taskId,
            int userId,
            int managerId) throws SQLException {

        String sql = """
                INSERT INTO task_assignments (task_id, user_id)
                SELECT t.task_id, pm.user_id
                FROM tasks t
                JOIN projects p
                    ON p.project_id = t.project_id
                JOIN project_members pm
                    ON pm.project_id = p.project_id
                JOIN users u
                    ON u.user_id = pm.user_id
                   AND u.role = 'MEMBER'
                WHERE p.project_id = ?
                  AND p.manager_id = ?
                  AND t.task_id = ?
                  AND pm.user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, managerId);
            statement.setInt(3, taskId);
            statement.setInt(4, userId);

            int insertedRows = statement.executeUpdate();

            if (insertedRows > 0) {
                return AssignmentResult.ASSIGNED;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // A concurrent request may have inserted the same composite key.
            if (isAssigned(taskId, userId)) {
                return AssignmentResult.ALREADY_ASSIGNED;
            }
            throw e;
        }

        // No row was inserted. Distinguish an invalid project/task/member
        // combination from a duplicate for a useful controller response.
        if (!isValidAssignmentTarget(projectId, taskId, userId, managerId)) {
            return AssignmentResult.INVALID_TASK_OR_MEMBER;
        }

        if (isAssigned(taskId, userId)) {
            return AssignmentResult.ALREADY_ASSIGNED;
        }

        return AssignmentResult.INVALID_TASK_OR_MEMBER;
    }

    private boolean isValidAssignmentTarget(
            int projectId,
            int taskId,
            int userId,
            int managerId) throws SQLException {

        String sql = """
                SELECT 1
                FROM tasks t
                JOIN projects p
                    ON p.project_id = t.project_id
                JOIN project_members pm
                    ON pm.project_id = p.project_id
                JOIN users u
                    ON u.user_id = pm.user_id
                   AND u.role = 'MEMBER'
                WHERE p.project_id = ?
                  AND p.manager_id = ?
                  AND t.task_id = ?
                  AND pm.user_id = ?
                LIMIT 1
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, managerId);
            statement.setInt(3, taskId);
            statement.setInt(4, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean isAssigned(int taskId, int userId) throws SQLException {
        String sql = """
                SELECT 1
                FROM task_assignments
                WHERE task_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean unassignTask(int taskId, int userId) throws SQLException {
        String sql = """
                DELETE FROM task_assignments
                WHERE task_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        }
    }

    public List<Integer> getAssigneeIds(int taskId) throws SQLException {
        String sql = """
                SELECT user_id
                FROM task_assignments
                WHERE task_id = ?
                ORDER BY user_id
                """;

        List<Integer> ids = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ids.add(resultSet.getInt("user_id"));
                }
            }
        }

        return ids;
    }
}
