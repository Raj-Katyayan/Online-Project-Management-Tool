package com.jvmcrew.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class SessionSecurityFilter implements Filter {

    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;


        String contextPath =
                httpRequest.getContextPath();

        String requestUri =
                httpRequest.getRequestURI();


        /*
         * Remove the application context path so we can
         * identify protected sections cleanly.
         */
        String path =
                requestUri.substring(
                        contextPath.length()
                );


        boolean protectedPage =
                path.startsWith("/admin/")
                || path.startsWith("/manager/")
                || path.startsWith("/member/");


        if (protectedPage) {

            /*
             * Do not allow sensitive authenticated pages
             * to be reused from browser cache.
             */
            httpResponse.setHeader(
                    "Cache-Control",
                    "no-store, no-cache, must-revalidate, max-age=0"
            );

            httpResponse.setHeader(
                    "Pragma",
                    "no-cache"
            );

            httpResponse.setDateHeader(
                    "Expires",
                    0
            );
        }


        /*
         * Apply a finite inactivity timeout to the
         * current session.
         */
        HttpSession session =
                httpRequest.getSession(false);


        if (session != null) {

            session.setMaxInactiveInterval(
                    SESSION_TIMEOUT_SECONDS
            );
        }


        chain.doFilter(
                request,
                response
        );
    }
}