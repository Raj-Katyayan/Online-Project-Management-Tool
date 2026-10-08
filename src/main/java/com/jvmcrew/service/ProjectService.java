package com.jvmcrew.service;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.exception.ValidationException;
import com.jvmcrew.model.Project;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

public class ProjectService implements ProjectServiceInterface {

    private final ProjectDAO projectDAO = new ProjectDAO();

    private static final Set<String> VALID_PRIORITIES =
            Set.of("LOW", "MEDIUM", "HIGH");

    private static final Set<String> VALID_STATUSES =
            Set.of("PLANNED", "IN_PROGRESS", "COMPLETED", "ON_HOLD");


    /*
     * Creates a new project after applying business validation.
     */
    @Override
    public boolean createProject(Project project)
            throws SQLException, ValidationException {

        validateProject(project);

        return projectDAO.createProject(project);
    }


    /*
     * Business validation for project creation.
     */
    private void validateProject(Project project)
            throws ValidationException {

        if (project == null) {
            throw new ValidationException(
                    "Project information is required."
            );
        }

        String projectName = project.getProjectName();

        if (projectName == null || projectName.isBlank()) {
            throw new ValidationException(
                    "Project name is required."
            );
        }

        projectName = projectName.trim();

        if (projectName.length() < 2) {
            throw new ValidationException(
                    "Project name must contain at least 2 characters."
            );
        }

        if (projectName.length() > 150) {
            throw new ValidationException(
                    "Project name cannot exceed 150 characters."
            );
        }

        project.setProjectName(projectName);


        if (project.getDescription() != null
                && project.getDescription().length() > 1000) {

            throw new ValidationException(
                    "Project description cannot exceed 1000 characters."
            );
        }


        LocalDate startDate = project.getStartDate();
        LocalDate endDate = project.getEndDate();

        if (startDate == null || endDate == null) {
            throw new ValidationException(
                    "Project start date and end date are required."
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new ValidationException(
                    "End date cannot be before start date."
            );
        }


        String priority = project.getPriority();

        if (priority == null || priority.isBlank()) {
            project.setPriority("MEDIUM");
        } else {
            priority = priority.trim().toUpperCase();

            if (!VALID_PRIORITIES.contains(priority)) {
                throw new ValidationException(
                        "Invalid project priority."
                );
            }

            project.setPriority(priority);
        }


        String status = project.getStatus();

        if (status == null || status.isBlank()) {
            project.setStatus("PLANNED");
        } else {
            status = status.trim().toUpperCase();

            if (!VALID_STATUSES.contains(status)) {
                throw new ValidationException(
                        "Invalid project status."
                );
            }

            project.setStatus(status);
        }


        if (project.getManagerId() <= 0) {
            throw new ValidationException(
                    "Invalid manager information."
            );
        }
    }
}