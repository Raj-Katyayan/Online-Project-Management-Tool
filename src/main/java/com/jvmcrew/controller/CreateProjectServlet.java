package com.jvmcrew.controller;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.model.Project;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/manager/create-project")
public class CreateProjectServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();

    private boolean checkManager(HttpSession s) {
        return s != null && s.getAttribute("userId") != null && "MANAGER".equals(s.getAttribute("userRole"));
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!checkManager(request.getSession(false))) { response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return; }
        request.getRequestDispatcher("/manager/create-project.jsp").forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!checkManager(session)) { response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return; }
        String name = request.getParameter("projectName");
        String description = request.getParameter("description");
        String startText = request.getParameter("startDate");
        String endText = request.getParameter("endDate");
        String priority = request.getParameter("priority");
        if (name == null || name.isBlank() || startText == null || endText == null || startText.isBlank() || endText.isBlank()) {
            request.setAttribute("error", "Project name and dates are required.");
            request.getRequestDispatcher("/manager/create-project.jsp").forward(request, response); return;
        }
        try {
            LocalDate start = LocalDate.parse(startText), end = LocalDate.parse(endText);
            if (end.isBefore(start)) throw new IllegalArgumentException("End date cannot be before start date.");
            Project project = new Project(name.trim(), description, start, end,
                    priority == null ? "MEDIUM" : priority, "PLANNED", (Integer) session.getAttribute("userId"));
            if (projectDAO.createProject(project)) response.sendRedirect(request.getContextPath() + "/manager/dashboard");
            else throw new SQLException("Project creation failed.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/manager/create-project.jsp").forward(request, response);
        }
    }
}
