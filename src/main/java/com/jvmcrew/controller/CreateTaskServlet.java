package com.jvmcrew.controller;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.TaskDAO;
import com.jvmcrew.model.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/manager/create-task")
public class CreateTaskServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null || !"MANAGER".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            var p = projectDAO.getProjectForManager(projectId, (Integer) s.getAttribute("userId"));
            if (p == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found."); return; }
            request.setAttribute("project", p);
            request.getRequestDispatcher("/manager/create-task.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        }
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null || !"MANAGER".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            var p = projectDAO.getProjectForManager(projectId, (Integer) s.getAttribute("userId"));
            if (p == null) { response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot modify this project."); return; }
            String title = request.getParameter("title"), description = request.getParameter("description");
            LocalDate start = LocalDate.parse(request.getParameter("startDate"));
            LocalDate due = LocalDate.parse(request.getParameter("dueDate"));
            if (title == null || title.isBlank() || due.isBefore(start)) throw new IllegalArgumentException("Task title is required and due date must not be before start date.");
            Task task = new Task(projectId, title.trim(), description,
                    request.getParameter("priority"), "TODO", start, due);
            if (!taskDAO.createTask(task)) throw new IllegalStateException("Task creation failed.");
            response.sendRedirect(request.getContextPath() + "/manager/project?id=" + projectId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/manager/create-task.jsp").forward(request, response);
        }
    }
}
