package com.jvmcrew.controller;

import com.jvmcrew.dao.ActivityLogDAO;
import com.jvmcrew.exception.ProjectNotFoundException;
import com.jvmcrew.exception.ValidationException;
import com.jvmcrew.service.ProjectStatusService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/manager/update-project-status")
public class ProjectStatusServlet extends HttpServlet {

    private final ProjectStatusService statusService =
            new ProjectStatusService();

    private final ActivityLogDAO activityLogDAO =
            new ActivityLogDAO();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * Authentication check.
         */
        if (session == null
                || session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login"
            );

            return;
        }


        /*
         * Authorization check.
         */
        if (!"MANAGER".equals(
                session.getAttribute("userRole"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }


        try {

            int managerId =
                    (Integer) session.getAttribute("userId");


            String projectIdParameter =
                    request.getParameter("projectId");

            String status =
                    request.getParameter("status");


            if (projectIdParameter == null
                    || projectIdParameter.isBlank()) {

                redirectWithError(
                        request,
                        response,
                        "Project ID is required."
                );

                return;
            }


            int projectId;

            try {

                projectId =
                        Integer.parseInt(
                                projectIdParameter
                        );

            } catch (NumberFormatException e) {

                redirectWithError(
                        request,
                        response,
                        "Invalid project ID."
                );

                return;
            }


            boolean updated =
                    statusService.updateStatus(
                            projectId,
                            managerId,
                            status
                    );


            if (!updated) {

                redirectWithError(
                        request,
                        response,
                        "Project status could not be updated."
                );

                return;
            }


            /*
             * Record the status change
             * in the activity log.
             */
            activityLogDAO.log(
                    managerId,
                    projectId,
                    "PROJECT_STATUS_UPDATED",
                    "Project status changed to "
                            + status.trim().toUpperCase()
            );


            response.sendRedirect(
                    request.getContextPath()
                    + "/manager/dashboard?statusUpdated=true"
            );


        } catch (ValidationException e) {

            redirectWithError(
                    request,
                    response,
                    e.getMessage()
            );


        } catch (ProjectNotFoundException e) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    e.getMessage()
            );


        } catch (SQLException e) {

            e.printStackTrace();

            redirectWithError(
                    request,
                    response,
                    "Database error while updating project status."
            );
        }
    }


    private void redirectWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String message)
            throws IOException {

        String encodedMessage =
                URLEncoder.encode(
                        message,
                        StandardCharsets.UTF_8
                );

        response.sendRedirect(
                request.getContextPath()
                + "/manager/dashboard?error="
                + encodedMessage
        );
    }
}