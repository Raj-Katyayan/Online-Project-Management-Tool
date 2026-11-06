package com.jvmcrew.controller;

import com.jvmcrew.exception.ValidationException;
import com.jvmcrew.model.Project;
import com.jvmcrew.service.ProjectService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/manager/create-project")
public class CreateProjectServlet extends HttpServlet {

    private final ProjectService projectService = new ProjectService();


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
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );
            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/manager/create-project.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!checkManager(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );
            return;
        }


        String name = request.getParameter("projectName");
        String description = request.getParameter("description");
        String startText = request.getParameter("startDate");
        String endText = request.getParameter("endDate");
        String priority = request.getParameter("priority");


        try {

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "Project name is required."
                );
            }

            if (startText == null || startText.isBlank()
                    || endText == null || endText.isBlank()) {

                throw new IllegalArgumentException(
                        "Project start date and end date are required."
                );
            }


            LocalDate startDate;
            LocalDate endDate;

            try {
                startDate = LocalDate.parse(startText);
                endDate = LocalDate.parse(endText);

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Please enter valid project dates."
                );
            }


            int managerId = (Integer) session.getAttribute("userId");


            Project project = new Project(
                    name.trim(),
                    description,
                    startDate,
                    endDate,
                    priority,
                    "PLANNED",
                    managerId
            );


            boolean created = projectService.createProject(project);


            if (created) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/manager/dashboard"
                );

                return;
            }


            throw new SQLException(
                    "Project creation failed."
            );


        } catch (ValidationException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/create-project.jsp"
            ).forward(request, response);


        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/create-project.jsp"
            ).forward(request, response);


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to create the project right now. Please try again."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/create-project.jsp"
            ).forward(request, response);
        }
    }
}