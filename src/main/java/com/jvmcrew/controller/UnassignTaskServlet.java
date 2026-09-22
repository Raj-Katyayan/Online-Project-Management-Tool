package com.jvmcrew.controller;

import com.jvmcrew.dao.TaskAssignmentDAO;
import com.jvmcrew.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/manager/unassign-task")
public class UnassignTaskServlet extends HttpServlet {

    private final TaskAssignmentDAO assignmentDAO =
            new TaskAssignmentDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

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

            int projectId = Integer.parseInt(
                    request.getParameter("projectId")
            );

            int taskId = Integer.parseInt(
                    request.getParameter("taskId")
            );

            int userId = Integer.parseInt(
                    request.getParameter("userId")
            );

            int managerId =
                    (Integer) session.getAttribute("userId");


            /*
             * Verify that:
             *
             * 1. This project belongs to the logged-in manager.
             * 2. This task actually belongs to this project.
             *
             * This prevents a manager from unassigning
             * assignments belonging to another manager's project.
             */

            String verifySql =
                    "SELECT 1 " +
                    "FROM projects p " +
                    "JOIN tasks t " +
                    "ON t.project_id = p.project_id " +
                    "WHERE p.project_id = ? " +
                    "AND t.task_id = ? " +
                    "AND p.manager_id = ?";


            boolean allowed = false;


            try (
                Connection c = DBConnection.getConnection();
                PreparedStatement ps =
                        c.prepareStatement(verifySql)
            ) {

                ps.setInt(1, projectId);
                ps.setInt(2, taskId);
                ps.setInt(3, managerId);


                try (ResultSet rs = ps.executeQuery()) {

                    allowed = rs.next();

                }

            }


            if (!allowed) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You cannot modify this task."
                );

                return;
            }


            /*
             * Remove only this particular user
             * from this particular task.
             */

            assignmentDAO.unassignTask(
                    taskId,
                    userId
            );


            /*
             * Return to project details page.
             */

            response.sendRedirect(
                    request.getContextPath()
                    + "/manager/project?id="
                    + projectId
            );


        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid task, project, or user ID."
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not unassign task."
            );
        }
    }
}