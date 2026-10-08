package com.jvmcrew.service;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.ProjectMemberDAO;
import com.jvmcrew.dao.ProjectProgressDAO;
import com.jvmcrew.dao.TaskDAO;
import com.jvmcrew.model.Project;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ManagerDashboardService
        implements ManagerDashboardServiceInterface {

    private final ProjectDAO projectDAO =
            new ProjectDAO();

    private final TaskDAO taskDAO =
            new TaskDAO();

    private final ProjectMemberDAO projectMemberDAO =
            new ProjectMemberDAO();

    private final ProjectProgressDAO projectProgressDAO =
            new ProjectProgressDAO();


    /*
     * Loads all projects belonging to the manager.
     */
    @Override
    public List<Project> getProjects(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return projectDAO.getProjectsByManager(
                managerId
        );
    }


    /*
     * Returns the total number of projects
     * managed by the manager.
     */
    @Override
    public int getTotalProjects(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return projectDAO.getProjectCountByManager(
                managerId
        );
    }


    /*
     * Returns the total number of tasks
     * across the manager's projects.
     */
    @Override
    public int getTotalTasks(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return taskDAO.getTaskCountByManager(
                managerId
        );
    }


    /*
     * Returns the number of tasks currently
     * in progress.
     */
    @Override
    public int getInProgressTasks(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return taskDAO.getTaskCountByManagerAndStatus(
                managerId,
                "IN_PROGRESS"
        );
    }


    /*
     * Returns the number of completed tasks.
     */
    @Override
    public int getCompletedTasks(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return taskDAO.getTaskCountByManagerAndStatus(
                managerId,
                "COMPLETED"
        );
    }


    /*
     * Returns the number of unique team members
     * working across the manager's projects.
     */
    @Override
    public int getTeamMembers(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return projectMemberDAO
                .getUniqueMemberCountByManager(
                        managerId
                );
    }


    /*
     * Searches and filters the manager's projects.
     */
    @Override
    public List<Project> searchProjects(
            int managerId,
            String search,
            String status,
            String priority)
            throws SQLException {

        validateManagerId(managerId);

        return projectDAO.searchProjects(
                managerId,
                search,
                status,
                priority
        );
    }


    /*
     * Returns project completion percentages.
     *
     * Map key   = project ID
     * Map value = completion percentage
     */
    @Override
    public Map<Integer, Integer> getProjectProgress(
            int managerId)
            throws SQLException {

        validateManagerId(managerId);

        return projectProgressDAO.getProgressByManager(
                managerId
        );
    }


    /*
     * Validates the manager ID before
     * performing any business operation.
     */
    private void validateManagerId(
            int managerId) {

        if (managerId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid manager information."
            );
        }
    }
}