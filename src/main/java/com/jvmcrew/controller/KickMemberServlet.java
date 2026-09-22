package com.jvmcrew.controller;

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
import java.sql.SQLException;

@WebServlet("/manager/kick-member")
public class KickMemberServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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

        try {
            int projectId = Integer.parseInt(
                    request.getParameter("projectId")
            );

            int userId = Integer.parseInt(
                    request.getParameter("userId")
            );

            int managerId = (Integer) session.getAttribute("userId");

            try (Connection c = DBConnection.getConnection()) {

                c.setAutoCommit(false);

                try {

                    /*
                     * First make sure this project belongs
                     * to the logged-in manager.
                     */
                    String verifySql =
                            "SELECT 1 FROM projects " +
                            "WHERE project_id = ? AND manager_id = ?";

                    try (PreparedStatement ps =
                                 c.prepareStatement(verifySql)) {

                        ps.setInt(1, projectId);
                        ps.setInt(2, managerId);

                        if (!ps.executeQuery().next()) {

                            c.rollback();

                            response.sendError(
                                    HttpServletResponse.SC_FORBIDDEN,
                                    "You cannot modify this project."
                            );

                            return;
                        }
                    }


                    /*
                     * Remove the member's task assignments
                     * belonging to this project.
                     */
                    String removeAssignmentsSql =
                            "DELETE ta FROM task_assignments ta " +
                            "JOIN tasks t ON t.task_id = ta.task_id " +
                            "WHERE t.project_id = ? " +
                            "AND ta.user_id = ?";

                    try (PreparedStatement ps =
                                 c.prepareStatement(removeAssignmentsSql)) {

                        ps.setInt(1, projectId);
                        ps.setInt(2, userId);
                        ps.executeUpdate();
                    }


                    /*
                     * Remove the user from the project.
                     */
                    String removeMemberSql =
                            "DELETE FROM project_members " +
                            "WHERE project_id = ? AND user_id = ?";

                    int removed;

                    try (PreparedStatement ps =
                                 c.prepareStatement(removeMemberSql)) {

                        ps.setInt(1, projectId);
                        ps.setInt(2, userId);

                        removed = ps.executeUpdate();
                    }


                    if (removed > 0) {
                        c.commit();
                    } else {
                        c.rollback();
                    }


                    response.sendRedirect(
                            request.getContextPath()
                            + "/manager/project?id="
                            + projectId
                    );

                } catch (SQLException e) {

                    c.rollback();
                    throw e;
                }

            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid project or user ID."
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Could not remove member from project."
            );
        }
    }
}