package com.jvmcrew.controller;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.ProjectMemberDAO;
import com.jvmcrew.dao.TaskDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/dashboard")
public class ManagerDashboardServlet extends HttpServlet {

    private final ProjectDAO projectDAO =
            new ProjectDAO();

    private final TaskDAO taskDAO =
            new TaskDAO();

    private final ProjectMemberDAO projectMemberDAO =
            new ProjectMemberDAO();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * Check login.
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
         * Check manager role.
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


            /*
             * Existing project list.
             */
            request.setAttribute(
                    "projects",
                    projectDAO.getProjectsByManager(managerId)
            );


            /*
             * =========================
             * DASHBOARD STATISTICS
             * =========================
             */

            int totalProjects =
                    projectDAO.getProjectCountByManager(
                            managerId
                    );

            int totalTasks =
                    taskDAO.getTaskCountByManager(
                            managerId
                    );

            int inProgressTasks =
                    taskDAO.getTaskCountByManagerAndStatus(
                            managerId,
                            "IN_PROGRESS"
                    );

            int completedTasks =
                    taskDAO.getTaskCountByManagerAndStatus(
                            managerId,
                            "COMPLETED"
                    );

            int teamMembers =
                    projectMemberDAO.getUniqueMemberCountByManager(
                            managerId
                    );


            request.setAttribute(
                    "totalProjects",
                    totalProjects
            );

            request.setAttribute(
                    "totalTasks",
                    totalTasks
            );

            request.setAttribute(
                    "inProgressTasks",
                    inProgressTasks
            );

            request.setAttribute(
                    "completedTasks",
                    completedTasks
            );

            request.setAttribute(
                    "teamMembers",
                    teamMembers
            );


            request.getRequestDispatcher(
                    "/manager/dashboard.jsp"
            ).forward(request, response);


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Database Error: "
                    + e.getMessage()
            );

            request.getRequestDispatcher(
                    "/manager/dashboard.jsp"
            ).forward(request, response);
        }
    }
}