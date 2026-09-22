package com.jvmcrew.filter;

import com.jvmcrew.dao.ActivityLogDAO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class ActivityLoggingFilter implements Filter {

    private final ActivityLogDAO activityLogDAO = new ActivityLogDAO();

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        /*
         * Let the actual Servlet execute first.
         * We only create a log after the request reaches
         * the application.
         */
        chain.doFilter(servletRequest, servletResponse);


        /*
         * Only log POST requests.
         * This prevents normal page visits from filling
         * the activity_logs table.
         */
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return;
        }


        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {
            return;
        }


        int userId =
                (Integer) session.getAttribute("userId");


        String uri = request.getRequestURI();

        String contextPath =
                request.getContextPath();

        String route =
                uri.startsWith(contextPath)
                        ? uri.substring(contextPath.length())
                        : uri;


        String action = null;
        String description = null;


        /*
         * Try to get the project ID when the request
         * contains one.
         */
        Integer projectId = null;

        String projectIdParam =
                request.getParameter("projectId");

        if (projectIdParam != null
                && !projectIdParam.isBlank()) {

            try {
                projectId =
                        Integer.parseInt(projectIdParam);

            } catch (NumberFormatException ignored) {
                projectId = null;
            }
        }


        /*
         * Identify the important business actions.
         */
        switch (route) {

            case "/manager/create-project":

                action = "PROJECT_CREATED";

                description =
                        "A new project was created.";

                break;


            case "/manager/add-member":

                action = "MEMBER_ADDED";

                description =
                        "A member was added to a project.";

                break;


            case "/manager/kick-member":

                action = "MEMBER_KICKED";

                description =
                        "A member was removed from a project.";

                break;


            case "/manager/create-task":

                action = "TASK_CREATED";

                description =
                        "A new task was created.";

                break;


            case "/manager/assign-task":

                action = "TASK_ASSIGNED";

                description =
                        "A task was assigned to a member.";

                break;


            case "/manager/unassign-task":

                action = "TASK_UNASSIGNED";

                description =
                        "A task assignment was removed.";

                break;


            case "/member/update-task":

                action = "TASK_STATUS_UPDATED";

                description =
                        "A task status was updated by a member.";

                break;


            case "/admin/update-role":

                action = "ROLE_CHANGED";

                description =
                        "A user's system role was changed by an administrator.";

                break;


            default:

                /*
                 * Ignore login, register, dashboard visits,
                 * and any unrelated POST requests.
                 */

                return;
        }


        /*
         * Logging must never break the user's actual action.
         * If logging fails, print the error and let the
         * application continue normally.
         */
        try {

            activityLogDAO.log(
                    userId,
                    projectId,
                    action,
                    description
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}