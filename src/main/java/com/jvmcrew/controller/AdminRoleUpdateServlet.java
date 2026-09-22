package com.jvmcrew.controller;

import com.jvmcrew.dao.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Set;

@WebServlet("/admin/update-role")
public class AdminRoleUpdateServlet extends HttpServlet {
    private static final Set<String> ALLOWED = Set.of("ADMIN", "MANAGER", "MEMBER");
    private final UserDAO userDAO = new UserDAO();

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        if (s == null || s.getAttribute("userId") == null || !"ADMIN".equals(s.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied."); return;
        }
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String role = request.getParameter("role");
            if (!ALLOWED.contains(role)) throw new IllegalArgumentException("Invalid role.");
            userDAO.updateRole(userId, role);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not update role.");
        }
    }
}
