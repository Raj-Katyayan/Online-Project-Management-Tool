package com.jvmcrew.dao;

import com.jvmcrew.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectProgressDAO {

    /*
     * Calculates completion percentage for every project
     * owned by the manager.
     */
    public Map<Integer, Integer> getProgressByManager(
            int managerId) throws SQLException {

        String sql = """
                SELECT
                    p.project_id,
                    COUNT(t.task_id) AS total_tasks,
                    COALESCE(
                        SUM(
                            CASE
                                WHEN t.status = 'COMPLETED'
                                THEN 1
                                ELSE 0
                            END
                        ),
                        0
                    ) AS completed_tasks
                FROM projects p
                LEFT JOIN tasks t
                    ON p.project_id = t.project_id
                WHERE p.manager_id = ?
                GROUP BY p.project_id
                ORDER BY p.project_id
                """;

        Map<Integer, Integer> progressMap =
                new LinkedHashMap<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int projectId =
                            rs.getInt("project_id");

                    int totalTasks =
                            rs.getInt("total_tasks");

                    int completedTasks =
                            rs.getInt("completed_tasks");

                    int progress = 0;

                    if (totalTasks > 0) {

                        progress =
                                (completedTasks * 100)
                                / totalTasks;
                    }

                    progressMap.put(
                            projectId,
                            progress
                    );
                }
            }
        }

        return progressMap;
    }
}