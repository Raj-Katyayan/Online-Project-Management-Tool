package com.jvmcrew.controller;

import com.jvmcrew.dao.ActivityLogDAO;
import com.jvmcrew.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ActivityLogDAO activityLogDAO =
            new ActivityLogDAO();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession s =
                request.getSession(false);


        /*
         * Check login.
         */
        if (s == null
                || s.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login"
            );

            return;
        }


        /*
         * Check ADMIN role.
         */
        if (!"ADMIN".equals(
                s.getAttribute("userRole"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }


        try {

            /*
             * Existing user management data.
             */
            request.setAttribute(
                    "users",
                    userDAO.getAllUsers()
            );


            /*
             * Recent system activity.
             */
            request.setAttribute(
                    "activities",
                    activityLogDAO.getRecentLogs(15)
            );


            request.getRequestDispatcher(
                    "/admin/dashboard.jsp"
            ).forward(request, response);


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Database Error: "
                    + e.getMessage()
            );


            /*
             * Still try to load the page with
             * the error message.
             */
            request.getRequestDispatcher(
                    "/admin/dashboard.jsp"
            ).forward(request, response);
        }
    }
}