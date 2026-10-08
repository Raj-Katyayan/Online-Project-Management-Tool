package com.jvmcrew.service;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.TaskDAO;
import com.jvmcrew.model.Project;
import com.jvmcrew.model.Task;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

public class CreateTaskService implements CreateTaskServiceInterface{

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    private static final Set<String> VALID_PRIORITIES =
            Set.of("LOW", "MEDIUM", "HIGH");


    /*
     * Checks whether the project belongs to the manager.
     */
    public Project getManagerProject(
            int projectId,
            int managerId) throws SQLException {

        validateManagerId(managerId);

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid project ID."
            );
        }

        return projectDAO.getProjectForManager(
                projectId,
                managerId
        );
    }


    /*
     * Creates a task after applying business validation.
     */
    public boolean createTask(Task task) throws SQLException {

        validateTask(task);

        return taskDAO.createTask(task);
    }


    /*
     * Business validation for task creation.
     */
    private void validateTask(Task task) {

        if (task == null) {
            throw new IllegalArgumentException(
                    "Task information is required."
            );
        }


        if (task.getProjectId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid project information."
            );
        }


        String title = task.getTitle();

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Task title is required."
            );
        }

        title = title.trim();

        if (title.length() < 2) {
            throw new IllegalArgumentException(
                    "Task title must contain at least 2 characters."
            );
        }

        if (title.length() > 150) {
            throw new IllegalArgumentException(
                    "Task title cannot exceed 150 characters."
            );
        }

        task.setTitle(title);


        if (task.getDescription() != null
                && task.getDescription().length() > 1000) {

            throw new IllegalArgumentException(
                    "Task description cannot exceed 1000 characters."
            );
        }


        LocalDate startDate = task.getStartDate();
        LocalDate dueDate = task.getDueDate();

        if (startDate == null || dueDate == null) {
            throw new IllegalArgumentException(
                    "Task start date and due date are required."
            );
        }

        if (dueDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before start date."
            );
        }


        String priority = task.getPriority();

        if (priority == null || priority.isBlank()) {
            task.setPriority("MEDIUM");
        } else {

            priority = priority.trim().toUpperCase();

            if (!VALID_PRIORITIES.contains(priority)) {
                throw new IllegalArgumentException(
                        "Invalid task priority."
                );
            }

            task.setPriority(priority);
        }


        task.setStatus("TODO");
    }


    private void validateManagerId(int managerId) {

        if (managerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid manager information."
            );
        }
    }
}