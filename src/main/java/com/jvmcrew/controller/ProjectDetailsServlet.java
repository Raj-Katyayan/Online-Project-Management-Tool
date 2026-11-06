package com.jvmcrew.controller;

import com.jvmcrew.exception.ProjectNotFoundException;
import com.jvmcrew.service.ProjectDetailsService;
import com.jvmcrew.service.ProjectDetailsService.ProjectDetailsData;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/project")
public class ProjectDetailsServlet extends HttpServlet {

    private final ProjectDetailsService projectDetailsService =
            new ProjectDetailsService();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * =========================
         * AUTHENTICATION + ROLE
         * =========================
         */

        if (session == null
                || session.getAttribute("userId") == null
                || !"MANAGER".equals(
                        session.getAttribute("userRole"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );

            return;
        }


        try {

            /*
             * =========================
             * READ PROJECT ID
             * =========================
             */

            String projectIdText =
                    request.getParameter("id");


            if (projectIdText == null
                    || projectIdText.isBlank()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Project ID is required."
                );

                return;
            }


            int projectId =
                    Integer.parseInt(projectIdText);


            int managerId =
                    (Integer) session.getAttribute("userId");


            /*
             * =========================
             * SERVICE LAYER
             * =========================
             */

            ProjectDetailsData data =
                    projectDetailsService.getProjectDetails(
                            projectId,
                            managerId
                    );


            /*
             * =========================
             * SEND DATA TO JSP
             * =========================
             */

            request.setAttribute(
                    "project",
                    data.getProject()
            );

            request.setAttribute(
                    "members",
                    data.getMembers()
            );

            request.setAttribute(
                    "tasks",
                    data.getTasks()
            );

            request.setAttribute(
                    "assigneesByTask",
                    data.getAssigneesByTask()
            );


            /*
             * =========================
             * DISPLAY PAGE
             * =========================
             */

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/project-details.jsp"
            ).forward(request, response);


        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid project ID."
            );


        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );


        } catch (ProjectNotFoundException e) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    e.getMessage()
            );


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load project details right now."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/manager/project-details.jsp"
            ).forward(request, response);
        }
    }
}