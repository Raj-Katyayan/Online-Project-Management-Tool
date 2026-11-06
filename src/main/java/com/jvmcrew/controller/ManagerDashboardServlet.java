package com.jvmcrew.controller;

import com.jvmcrew.model.Project;
import com.jvmcrew.service.ManagerDashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/manager/dashboard")
public class ManagerDashboardServlet
        extends HttpServlet {

    private final ManagerDashboardService dashboardService =
            new ManagerDashboardService();


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
                    (Integer) session.getAttribute(
                            "userId"
                    );


            /*
             * Read search/filter parameters.
             */
            String search =
                    request.getParameter("search");

            String status =
                    request.getParameter("status");

            String priority =
                    request.getParameter("priority");


            /*
             * Load projects.
             */
            List<Project> projects;

            boolean hasFilters =
                    (search != null && !search.isBlank())
                    || (status != null
                        && !status.isBlank()
                        && !"ALL".equalsIgnoreCase(status))
                    || (priority != null
                        && !priority.isBlank()
                        && !"ALL".equalsIgnoreCase(priority));


            if (hasFilters) {

                projects =
                        dashboardService.searchProjects(
                                managerId,
                                search,
                                status,
                                priority
                        );

            } else {

                projects =
                        dashboardService.getProjects(
                                managerId
                        );
            }


            /*
             * =========================
             * DASHBOARD STATISTICS
             * =========================
             */

            int totalProjects =
                    dashboardService.getTotalProjects(
                            managerId
                    );

            int totalTasks =
                    dashboardService.getTotalTasks(
                            managerId
                    );

            int inProgressTasks =
                    dashboardService.getInProgressTasks(
                            managerId
                    );

            int completedTasks =
                    dashboardService.getCompletedTasks(
                            managerId
                    );

            int teamMembers =
                    dashboardService.getTeamMembers(
                            managerId
                    );


            /*
             * =========================
             * PROJECT PROGRESS
             * =========================
             */

            Map<Integer, Integer> projectProgress =
                    dashboardService.getProjectProgress(
                            managerId
                    );


            /*
             * Send data to JSP.
             */

            request.setAttribute(
                    "projects",
                    projects
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

            request.setAttribute(
                    "projectProgress",
                    projectProgress
            );


            /*
             * Preserve search/filter values.
             */
            request.setAttribute(
                    "search",
                    search == null
                            ? ""
                            : search
            );

            request.setAttribute(
                    "selectedStatus",
                    status == null
                            || status.isBlank()
                            ? "ALL"
                            : status
            );

            request.setAttribute(
                    "selectedPriority",
                    priority == null
                            || priority.isBlank()
                            ? "ALL"
                            : priority
            );


            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/dashboard.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/dashboard.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Database error while loading dashboard."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/dashboard.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }
}