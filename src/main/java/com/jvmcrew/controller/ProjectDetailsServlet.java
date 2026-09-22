package com.jvmcrew.controller;

import com.jvmcrew.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/project")
public class ProjectDetailsServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final ProjectMemberDAO memberDAO = new ProjectMemberDAO();
    private final TaskDAO taskDAO = new TaskDAO();
    private final TaskAssignmentDAO assignmentDAO = new TaskAssignmentDAO();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"MANAGER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int projectId = Integer.parseInt(request.getParameter("id"));
            int managerId = (Integer) session.getAttribute("userId");
            var project = projectDAO.getProjectForManager(projectId, managerId);
            if (project == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found."); return; }
            request.setAttribute("project", project);
            request.setAttribute("members", memberDAO.getMembersByProject(projectId));
            var tasks = taskDAO.getTasksByProject(projectId);
            request.setAttribute("tasks", tasks);
            var assigneesByTask = new java.util.HashMap<Integer, java.util.List<Integer>>();
            for (var task : tasks) {
                assigneesByTask.put(task.getTaskId(), assignmentDAO.getAssigneeIds(task.getTaskId()));
            }
            request.setAttribute("assigneesByTask", assigneesByTask);
            request.getRequestDispatcher("/manager/project-details.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database Error: " + e.getMessage());
            request.getRequestDispatcher("/manager/project-details.jsp").forward(request, response);
        }
    }
}
