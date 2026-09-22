package com.jvmcrew.controller;

import com.jvmcrew.dao.TaskDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/member/dashboard")
public class MemberDashboardServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); return;
        }
        if (!"MEMBER".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            request.setAttribute("tasks", taskDAO.getTasksByUser((Integer) s.getAttribute("userId")));
            request.getRequestDispatcher("/member/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database Error: " + e.getMessage());
            request.getRequestDispatcher("/member/dashboard.jsp").forward(request, response);
        }
    }
}
