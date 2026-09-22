package com.jvmcrew.controller;

import com.jvmcrew.dao.ActivityLogDAO;
import com.jvmcrew.dao.TaskDeleteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/manager/delete-task")
public class DeleteTaskServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        /*
         * Authentication + role check
         */
        if (session == null
                || session.getAttribute("userId") == null
                || !"MANAGER".equals(
                        session.getAttribute("userRole"))) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login.jsp"
            );

            return;
        }


        int managerId =
                (Integer) session.getAttribute("userId");


        String taskIdParam =
                request.getParameter("taskId");

        String projectIdParam =
                request.getParameter("projectId");


        /*
         * Validate parameters
         */
        if (taskIdParam == null
                || projectIdParam == null
                || taskIdParam.isBlank()
                || projectIdParam.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
            );

            return;
        }


        int taskId;
        int projectId;


        try {

            taskId =
                    Integer.parseInt(taskIdParam);

            projectId =
                    Integer.parseInt(projectIdParam);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/dashboard"
            );

            return;
        }


        try {

            TaskDeleteDAO taskDeleteDAO =
                    new TaskDeleteDAO();


            /*
             * This method also verifies that
             * the project belongs to this manager.
             */
            String taskTitle =
                    taskDeleteDAO.deleteTask(
                            taskId,
                            projectId,
                            managerId
                    );


            /*
             * Null means the security/ownership
             * check failed or the task wasn't deleted.
             */
            if (taskTitle == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/manager/project?id="
                                + projectId
                );

                return;
            }


            /*
             * Record the deletion in the
             * activity feed.
             */
            try {

                ActivityLogDAO activityLogDAO =
                        new ActivityLogDAO();

                activityLogDAO.log(
                        managerId,
                        projectId,
                        "TASK_DELETED",
                        "Task deleted: " + taskTitle
                );

            } catch (SQLException logException) {

                /*
                 * Deletion has already succeeded.
                 * Logging failure should not undo it.
                 */
                System.err.println(
                        "Activity log failed: "
                                + logException.getMessage()
                );
            }


            /*
             * Return to the project.
             */
            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/project?id="
                            + projectId
                            + "&deleted=true"
            );


        } catch (SQLException e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                            + "/manager/project?id="
                            + projectId
                            + "&error=Unable+to+delete+task"
            );
        }
    }
}