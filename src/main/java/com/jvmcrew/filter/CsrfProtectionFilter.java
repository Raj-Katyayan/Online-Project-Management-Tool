package com.jvmcrew.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;

@WebFilter("/*")
public class CsrfProtectionFilter implements Filter {

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


        String method =
                httpRequest.getMethod();


        /*
         * GET, HEAD and OPTIONS do not change application state,
         * so CSRF validation is not required for them.
         */
        if ("GET".equalsIgnoreCase(method)
                || "HEAD".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method)) {

            chain.doFilter(request, response);

            return;
        }


        /*
         * For state-changing requests, verify that the request
         * originated from this application.
         */
        String origin =
                httpRequest.getHeader("Origin");

        String referer =
                httpRequest.getHeader("Referer");


        String expectedOrigin =
                buildExpectedOrigin(httpRequest);


        boolean validOrigin = false;


        /*
         * Prefer Origin when the browser supplies it.
         */
        if (origin != null
                && !origin.isBlank()) {

            validOrigin =
                    expectedOrigin.equalsIgnoreCase(
                            origin.trim()
                    );

        } else if (referer != null
                && !referer.isBlank()) {

            /*
             * Fall back to Referer when Origin is absent.
             */
            try {

                URI refererUri =
                        URI.create(referer);

                String refererOrigin =
                        refererUri.getScheme()
                                + "://"
                                + refererUri.getAuthority();

                validOrigin =
                        expectedOrigin.equalsIgnoreCase(
                                refererOrigin
                        );

            } catch (IllegalArgumentException e) {

                validOrigin = false;
            }

        }


        /*
         * Reject cross-site state-changing requests.
         */
        if (!validOrigin) {

            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "CSRF validation failed."
            );

            return;
        }


        chain.doFilter(request, response);
    }


    private String buildExpectedOrigin(
            HttpServletRequest request) {

        String scheme =
                request.getScheme();

        String serverName =
                request.getServerName();

        int serverPort =
                request.getServerPort();


        boolean defaultPort =
                ("http".equalsIgnoreCase(scheme)
                        && serverPort == 80)
                || ("https".equalsIgnoreCase(scheme)
                        && serverPort == 443);


        if (defaultPort) {

            return scheme
                    + "://"
                    + serverName;

        }


        return scheme
                + "://"
                + serverName
                + ":"
                + serverPort;
    }
}