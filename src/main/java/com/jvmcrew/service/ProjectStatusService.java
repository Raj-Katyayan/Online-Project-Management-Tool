package com.jvmcrew.service;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.ProjectStatusDAO;
import com.jvmcrew.exception.ProjectNotFoundException;
import com.jvmcrew.exception.ValidationException;

import java.sql.SQLException;
import java.util.Set;

public class ProjectStatusService
        implements ProjectStatusServiceInterface {

    private final ProjectDAO projectDAO =
            new ProjectDAO();

    private final ProjectStatusDAO projectStatusDAO =
            new ProjectStatusDAO();


    private static final Set<String> ALLOWED_STATUSES =
            Set.of(
                    "PLANNED",
                    "IN_PROGRESS",
                    "COMPLETED",
                    "ON_HOLD"
            );


    @Override
    public boolean updateStatus(
            int projectId,
            int managerId,
            String status)
            throws SQLException,
                   ValidationException,
                   ProjectNotFoundException {

        validateIds(projectId, managerId);

        if (status == null || status.isBlank()) {

            throw new ValidationException(
                    "Project status is required."
            );
        }

        String normalizedStatus =
                status.trim().toUpperCase();

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {

            throw new ValidationException(
                    "Invalid project status."
            );
        }


        /*
         * Verify that the project belongs
         * to the logged-in manager.
         */
        if (projectDAO.getProjectForManager(
                projectId,
                managerId
        ) == null) {

            throw new ProjectNotFoundException(
                    "Project not found or you do not have access to this project."
            );
        }


        return projectStatusDAO.updateStatus(
                projectId,
                managerId,
                normalizedStatus
        );
    }


    private void validateIds(
            int projectId,
            int managerId)
            throws ValidationException {

        if (projectId <= 0) {

            throw new ValidationException(
                    "Invalid project information."
            );
        }

        if (managerId <= 0) {

            throw new ValidationException(
                    "Invalid manager information."
            );
        }
    }
}