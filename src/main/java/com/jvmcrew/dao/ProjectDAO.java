package com.jvmcrew.dao;

import com.jvmcrew.model.Project;
import com.jvmcrew.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public boolean createProject(Project project) throws SQLException {
        String sql = """
                INSERT INTO projects
                (project_name, description, start_date, end_date, priority, status, manager_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getDescription());
            ps.setObject(3, project.getStartDate());
            ps.setObject(4, project.getEndDate());
            ps.setString(5, project.getPriority());
            ps.setString(6, project.getStatus());
            ps.setInt(7, project.getManagerId());

            return ps.executeUpdate() > 0;
        }
    }


    public List<Project> getProjectsByManager(int managerId)
            throws SQLException {

        String sql = """
                SELECT project_id, project_name, description,
                       start_date, end_date, priority, status, manager_id
                FROM projects
                WHERE manager_id = ?
                ORDER BY created_at DESC
                """;

        List<Project> projects = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    projects.add(mapProject(rs));
                }
            }
        }

        return projects;
    }
	
	public List<Project> searchProjects(
        int managerId,
        String search,
        String status,
        String priority) throws SQLException {

    StringBuilder sql = new StringBuilder("""
            SELECT project_id, project_name, description,
                   start_date, end_date, priority, status, manager_id
            FROM projects
            WHERE manager_id = ?
            """);

    List<Object> parameters = new ArrayList<>();

    parameters.add(managerId);

    if (search != null && !search.isBlank()) {
        sql.append("""
                AND (
                    project_name LIKE ?
                    OR description LIKE ?
                )
                """);

        String searchPattern = "%" + search.trim() + "%";

        parameters.add(searchPattern);
        parameters.add(searchPattern);
    }

    if (status != null && !status.isBlank()
            && !"ALL".equalsIgnoreCase(status)) {

        sql.append(" AND status = ?");
        parameters.add(status.trim().toUpperCase());
    }

    if (priority != null && !priority.isBlank()
            && !"ALL".equalsIgnoreCase(priority)) {

        sql.append(" AND priority = ?");
        parameters.add(priority.trim().toUpperCase());
    }

    sql.append(" ORDER BY created_at DESC");

    List<Project> projects = new ArrayList<>();

    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql.toString())) {

        for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(i + 1, parameters.get(i));
        }

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                projects.add(mapProject(rs));
            }
        }
    }

    return projects;
}
	
    public Project getProjectForManager(
            int projectId,
            int managerId) throws SQLException {

        String sql = """
                SELECT project_id, project_name, description,
                       start_date, end_date, priority, status, manager_id
                FROM projects
                WHERE project_id = ? AND manager_id = ?
                """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, managerId);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next()
                        ? mapProject(rs)
                        : null;
            }
        }
    }


    /*
     * =========================
     * DASHBOARD STATISTICS
     * =========================
     */

    public int getProjectCountByManager(int managerId)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) " +
                "FROM projects " +
                "WHERE manager_id = ?";

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


    private Project mapProject(ResultSet rs)
            throws SQLException {

        Project p = new Project();

        p.setProjectId(
                rs.getInt("project_id")
        );

        p.setProjectName(
                rs.getString("project_name")
        );

        p.setDescription(
                rs.getString("description")
        );

        p.setStartDate(
                rs.getObject(
                        "start_date",
                        java.time.LocalDate.class
                )
        );

        p.setEndDate(
                rs.getObject(
                        "end_date",
                        java.time.LocalDate.class
                )
        );

        p.setPriority(
                rs.getString("priority")
        );

        p.setStatus(
                rs.getString("status")
        );

        p.setManagerId(
                rs.getInt("manager_id")
        );

        return p;
    }
}