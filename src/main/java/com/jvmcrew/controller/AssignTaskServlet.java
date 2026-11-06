package com.jvmcrew.controller;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.TaskAssignmentDAO;
import com.jvmcrew.dao.TaskAssignmentDAO.AssignmentResult;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/manager/assign-task")
public class AssignTaskServlet extends HttpServlet {

    private final ProjectDAO projectDAO = new ProjectDAO();
    private final TaskAssignmentDAO assignmentDAO = new TaskAssignmentDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"MANAGER".equals(session.getAttribute("userRole"))) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );
            return;
        }

        Integer projectId = parsePositiveInteger(
                request.getParameter("projectId")
        );
        Integer taskId = parsePositiveInteger(
                request.getParameter("taskId")
        );
        Integer userId = parsePositiveInteger(
                request.getParameter("userId")
        );

        if (projectId == null || taskId == null || userId == null) {
            if (projectId != null) {
                redirectWithError(
                        request,
                        response,
                        projectId,
                        "Select a valid project, task and team member."
                );
            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid project, task or member ID."
                );
            }
            return;
        }

        int managerId = (Integer) session.getAttribute("userId");

        try {
            // Controller-level authorization gives a clear 403 response.
            // The DAO repeats the ownership condition inside its INSERT SQL
            // so the write is protected even if called from another code path.
            if (projectDAO.getProjectForManager(projectId, managerId) == null) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You cannot modify this project."
                );
                return;
            }

            AssignmentResult result = assignmentDAO.assignTaskToProjectMember(
                    projectId,
                    taskId,
                    userId,
                    managerId
            );

            switch (result) {
                case ASSIGNED -> response.sendRedirect(
                        request.getContextPath()
                                + "/manager/project?id=" + projectId
                );

                case ALREADY_ASSIGNED -> redirectWithError(
                        request,
                        response,
                        projectId,
                        "This task is already assigned to the selected member."
                );

                case INVALID_TASK_OR_MEMBER -> redirectWithError(
                        request,
                        response,
                        projectId,
                        "The selected task must belong to this project and the selected user must be a member of this project."
                );
            }

        } catch (SQLException e) {
            getServletContext().log(
                    "Database error while assigning task " + taskId
                            + " in project " + projectId,
                    e
            );
            redirectWithError(
                    request,
                    response,
                    projectId,
                    "The task could not be assigned because of a database error. Please try again."
            );
        }
    }

    private Integer parsePositiveInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void redirectWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            int projectId,
            String message) throws IOException {

        String encodedMessage = URLEncoder.encode(
                message,
                StandardCharsets.UTF_8
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/manager/project?id=" + projectId
                        + "&error=" + encodedMessage
        );
    }
}
