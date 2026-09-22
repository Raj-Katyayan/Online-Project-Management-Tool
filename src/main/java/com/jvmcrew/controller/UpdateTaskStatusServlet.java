package com.jvmcrew.controller;

import com.jvmcrew.dao.TaskDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebServlet("/member/update-task")
public class UpdateTaskStatusServlet extends HttpServlet {
    private static final Set<String> ALLOWED = Set.of("TODO", "IN_PROGRESS", "COMPLETED", "BLOCKED");
    private final TaskDAO taskDAO = new TaskDAO();

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null || !"MEMBER".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String status = request.getParameter("status");
            if (!ALLOWED.contains(status)) throw new IllegalArgumentException("Invalid task status.");
            taskDAO.updateStatusForUser(taskId, (Integer) s.getAttribute("userId"), status);
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not update task.");
        }
    }
}
