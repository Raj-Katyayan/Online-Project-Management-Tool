package com.jvmcrew.controller;

import com.jvmcrew.dao.ActivityLogDAO;
import com.jvmcrew.dao.ProjectDeleteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/delete-project")
public class DeleteProjectServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * Authentication + role check
         */
        if (session == null
                || session.getAttribute("userId") == null
                || !"MANAGER".equals(
                        session.getAttribute("userRole"))) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login.jsp"
            );

            return;
        }


        int managerId =
                (Integer) session.getAttribute("userId");


        String projectIdParam =
                request.getParameter("projectId");


        if (projectIdParam == null
                || projectIdParam.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
            );

            return;
        }


        int projectId;


        try {

            projectId =
                    Integer.parseInt(projectIdParam);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
            );

            return;
        }


        try {

            ProjectDeleteDAO projectDeleteDAO =
                    new ProjectDeleteDAO();


            /*
             * DAO verifies that this project
             * belongs to this manager.
             */
            String projectName =
                    projectDeleteDAO.deleteProject(
                            projectId,
                            managerId
                    );


            /*
             * Null means the project was not found
             * or does not belong to this manager.
             */
            if (projectName == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/manager/dashboard"
                );

                return;
            }


            /*
             * Add an audit entry AFTER deletion.
             *
             * projectId is null because the project
             * no longer exists.
             */
            try {

                ActivityLogDAO activityLogDAO =
                        new ActivityLogDAO();

                activityLogDAO.log(
                        managerId,
                        null,
                        "PROJECT_DELETED",
                        "Project deleted: " + projectName
                );

            } catch (SQLException logException) {

                System.err.println(
                        "Activity log failed: "
                                + logException.getMessage()
                );
            }


            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
                            + "?deleted=project"
            );


        } catch (SQLException e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
                            + "?error=Unable+to+delete+project"
            );
        }
    }
}