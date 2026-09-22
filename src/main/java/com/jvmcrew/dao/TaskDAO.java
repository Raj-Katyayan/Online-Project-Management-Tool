package com.jvmcrew.dao;

import com.jvmcrew.model.Task;
import com.jvmcrew.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public boolean createTask(Task task) throws SQLException {

        String sql = """
                INSERT INTO tasks
                (project_id, title, description, priority, status, start_date, due_date)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, task.getProjectId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getPriority());
            ps.setString(5, task.getStatus());
            ps.setObject(6, task.getStartDate());
            ps.setObject(7, task.getDueDate());

            return ps.executeUpdate() > 0;
        }
    }


    public List<Task> getTasksByProject(int projectId)
            throws SQLException {

        String sql = """
                SELECT t.task_id, t.project_id, p.project_name,
                       t.title, t.description, t.priority, t.status,
                       t.start_date, t.due_date
                FROM tasks t
                JOIN projects p ON t.project_id = p.project_id
                WHERE t.project_id = ?
                ORDER BY t.due_date, t.task_id
                """;

        return queryTasks(sql, projectId);
    }


    public List<Task> getTasksByUser(int userId)
            throws SQLException {

        String sql = """
                SELECT t.task_id, t.project_id, p.project_name,
                       t.title, t.description, t.priority, t.status,
                       t.start_date, t.due_date
                FROM tasks t
                JOIN task_assignments ta
                    ON t.task_id = ta.task_id
                JOIN projects p
                    ON t.project_id = p.project_id
                WHERE ta.user_id = ?
                ORDER BY t.due_date, t.task_id
                """;

        return queryTasks(sql, userId);
    }


    public boolean updateStatusForUser(
            int taskId,
            int userId,
            String status) throws SQLException {

        String sql = """
                UPDATE tasks t
                JOIN task_assignments ta
                    ON t.task_id = ta.task_id
                SET t.status = ?
                WHERE t.task_id = ? AND ta.user_id = ?
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, taskId);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;
        }
    }


    /*
     * =========================
     * DASHBOARD STATISTICS
     * =========================
     */

    public int getTaskCountByManager(int managerId)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM tasks t
                JOIN projects p
                    ON t.project_id = p.project_id
                WHERE p.manager_id = ?
                """;

        return getCount(sql, managerId);
    }


    public int getTaskCountByManagerAndStatus(
            int managerId,
            String status) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM tasks t
                JOIN projects p
                    ON t.project_id = p.project_id
                WHERE p.manager_id = ?
                  AND t.status = ?
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }


    private int getCount(String sql, int managerId)
            throws SQLException {

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


    private List<Task> queryTasks(String sql, int id)
            throws SQLException {

        List<Task> tasks = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Task task = new Task();

                    task.setTaskId(
                            rs.getInt("task_id")
                    );

                    task.setProjectId(
                            rs.getInt("project_id")
                    );

                    task.setProjectName(
                            rs.getString("project_name")
                    );

                    task.setTitle(
                            rs.getString("title")
                    );

                    task.setDescription(
                            rs.getString("description")
                    );

                    task.setPriority(
                            rs.getString("priority")
                    );

                    task.setStatus(
                            rs.getString("status")
                    );

                    task.setStartDate(
                            rs.getObject(
                                    "start_date",
                                    java.time.LocalDate.class
                            )
                    );

                    task.setDueDate(
                            rs.getObject(
                                    "due_date",
                                    java.time.LocalDate.class
                            )
                    );

                    tasks.add(task);
                }
            }
        }

        return tasks;
    }
}