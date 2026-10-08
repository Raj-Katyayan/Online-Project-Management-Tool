package com.jvmcrew.controller;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.ProjectMemberDAO;
import com.jvmcrew.dao.TaskAssignmentDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/manager/assign-task")
public class AssignTaskServlet extends HttpServlet {

    private final ProjectDAO projectDAO =
            new ProjectDAO();

    private final ProjectMemberDAO projectMemberDAO =
            new ProjectMemberDAO();

    private final TaskAssignmentDAO assignmentDAO =
            new TaskAssignmentDAO();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * =========================
         * AUTHENTICATION + ROLE
         * =========================
         */

        if (session == null
                || session.getAttribute("userId") == null
                || !"MANAGER".equals(
                        session.getAttribute("userRole"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }


        try {

            /*
             * =========================
             * READ REQUEST PARAMETERS
             * =========================
             */

            String projectIdText =
                    request.getParameter("projectId");

            String taskIdText =
                    request.getParameter("taskId");

            String userIdText =
                    request.getParameter("userId");


            /*
             * =========================
             * BASIC VALIDATION
             * =========================
             */

            if (projectIdText == null
                    || projectIdText.isBlank()
                    || taskIdText == null
                    || taskIdText.isBlank()) {

                redirectWithError(
                        request,
                        response,
                        "Invalid project or task information."
                );

                return;
            }


            int projectId =
                    Integer.parseInt(projectIdText);

            int taskId =
                    Integer.parseInt(taskIdText);

            int managerId =
                    (Integer) session.getAttribute("userId");


            /*
             * =========================
             * PROJECT OWNERSHIP CHECK
             * =========================
             */

            if (projectDAO.getProjectForManager(
                    projectId,
                    managerId
            ) == null) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You cannot modify this project."
                );

                return;
            }


            /*
             * =========================
             * NO MEMBER SELECTED
             * =========================
             *
             * If userId is empty, determine whether
             * the project has any team members.
             */

            if (userIdText == null
                    || userIdText.isBlank()) {

                boolean hasMembers =
                        !projectMemberDAO
                                .getMembersByProject(projectId)
                                .isEmpty();


                if (!hasMembers) {

                    redirectWithError(
                            request,
                            response,
                            "No team members have been added to this project. Please add a team member first."
                    );

                } else {

                    redirectWithError(
                            request,
                            response,
                            "Please select a team member to assign this task."
                    );
                }

                return;
            }


            /*
             * =========================
             * MEMBER SELECTION
             * =========================
             */

            int userId =
                    Integer.parseInt(userIdText);


            /*
             * =========================
             * ASSIGN TASK
             * =========================
             */

            assignmentDAO.assignTask(
                    taskId,
                    userId
            );


            /*
             * =========================
             * SUCCESS
             * =========================
             */

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/project?id="
                            + projectId
            );


        } catch (NumberFormatException e) {

            redirectWithError(
                    request,
                    response,
                    "Invalid project, task, or member information."
            );


        } catch (Exception e) {

            e.printStackTrace();

            redirectWithError(
                    request,
                    response,
                    "Could not assign task."
            );
        }
    }


    /*
     * =========================
     * ERROR REDIRECT
     * =========================
     *
     * Sends the manager back to the project page
     * with a readable error message.
     */

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


        String projectId =
                request.getParameter("projectId");


        response.sendRedirect(
                request.getContextPath()
                        + "/manager/project?id="
                        + projectId
                        + "&error="
                        + encodedMessage
        );
    }
}