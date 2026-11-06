package com.jvmcrew.controller;

import com.jvmcrew.model.Project;
import com.jvmcrew.model.Task;
import com.jvmcrew.service.CreateTaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/manager/create-task")
public class CreateTaskServlet extends HttpServlet {

    private final CreateTaskService taskService =
            new CreateTaskService();

    private boolean checkManager(HttpSession session) {
        return session != null
                && session.getAttribute("userId") != null
                && "MANAGER".equals(session.getAttribute("userRole"));
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!checkManager(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        try {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            if (projectId <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
                return;
            }

            int managerId = (Integer) session.getAttribute("userId");
            Project project = taskService.getManagerProject(projectId, managerId);

            if (project == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
                return;
            }

            request.setAttribute("project", project);
            request.getRequestDispatcher("/WEB-INF/views/manager/create-task.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
        } catch (Exception e) {
            getServletContext().log("Unable to load the project task form.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load project."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!checkManager(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        final int projectId;
        try {
            projectId = Integer.parseInt(request.getParameter("projectId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
            return;
        }

        if (projectId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID.");
            return;
        }

        int managerId = (Integer) session.getAttribute("userId");
        final Project project;

        try {
            project = taskService.getManagerProject(projectId, managerId);
        } catch (Exception e) {
            getServletContext().log("Unable to verify project ownership.", e);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to verify project."
            );
            return;
        }

        if (project == null) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You cannot modify this project."
            );
            return;
        }

        // Preserve the project for the JSP if validation or creation fails.
        request.setAttribute("project", project);

        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startText = request.getParameter("startDate");
            String dueText = request.getParameter("dueDate");
            String priority = request.getParameter("priority");

            LocalDate startDate;
            LocalDate dueDate;
            try {
                startDate = LocalDate.parse(startText);
                dueDate = LocalDate.parse(dueText);
            } catch (Exception e) {
                throw new IllegalArgumentException("Please enter valid task dates.");
            }

            Task task = new Task(
                    projectId,
                    title,
                    description,
                    priority,
                    "TODO",
                    startDate,
                    dueDate
            );

            if (!taskService.createTask(task)) {
                throw new IllegalStateException("Task creation failed.");
            }

            response.sendRedirect(
                    request.getContextPath() + "/manager/project?id=" + projectId
            );

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/manager/create-task.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            getServletContext().log("Unable to create task.", e);
            request.setAttribute(
                    "error",
                    "Unable to create the task right now. Please try again."
            );
            request.getRequestDispatcher("/WEB-INF/views/manager/create-task.jsp")
                    .forward(request, response);
        }
    }
}
