package com.jvmcrew.controller;

import com.jvmcrew.dao.*;
import com.jvmcrew.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/manager/add-member")
public class AddMemberServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final ProjectMemberDAO memberDAO = new ProjectMemberDAO();
    private final UserDAO userDAO = new UserDAO();

    private Integer projectId(String value) {
        try { return Integer.valueOf(value); } catch (Exception e) { return null; }
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"MANAGER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        Integer pid = projectId(request.getParameter("projectId"));
        if (pid == null) { response.sendRedirect(request.getContextPath() + "/manager/dashboard"); return; }
        try {
            var project = projectDAO.getProjectForManager(pid, (Integer) session.getAttribute("userId"));
            if (project == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found."); return; }
            request.setAttribute("project", project);
            List<User> members = userDAO.getAllMembers();
            request.setAttribute("members", members);
            request.setAttribute("existingMemberIds", memberDAO.getMemberIds(pid));
            request.getRequestDispatcher("/WEB-INF/views/manager/add-member.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/manager/add-member.jsp").forward(request, response);
        }
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"MANAGER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            int userId = Integer.parseInt(request.getParameter("userId"));
            var project = projectDAO.getProjectForManager(projectId, (Integer) session.getAttribute("userId"));
            if (project == null) { response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot modify this project."); return; }
            if (memberDAO.addMember(projectId, userId)) {
                response.sendRedirect(request.getContextPath() + "/manager/project?id=" + projectId);
            } else {
                request.setAttribute("error", "Member is already added or could not be added.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
