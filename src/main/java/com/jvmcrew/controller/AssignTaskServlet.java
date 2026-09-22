package com.jvmcrew.controller;

import com.jvmcrew.dao.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/manager/assign-task")
public class AssignTaskServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final TaskAssignmentDAO assignmentDAO = new TaskAssignmentDAO();

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null || !"MANAGER".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            int userId = Integer.parseInt(request.getParameter("userId"));
            if (projectDAO.getProjectForManager(projectId, (Integer) s.getAttribute("userId")) == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot modify this project."); return;
            }
            assignmentDAO.assignTask(taskId, userId);
            response.sendRedirect(request.getContextPath() + "/manager/project?id=" + projectId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not assign task.");
        }
    }
}
