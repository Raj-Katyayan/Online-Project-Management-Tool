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

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;


        /*
         * Prevent browsers from MIME-sniffing responses.
         */
        httpResponse.setHeader(
                "X-Content-Type-Options",
                "nosniff"
        );


        /*
         * Prevent the application from being embedded
         * inside another site's frame.
         */
        httpResponse.setHeader(
                "X-Frame-Options",
                "SAMEORIGIN"
        );


        /*
         * Modern clickjacking control.
         * The application may frame itself, but not
         * be embedded by another origin.
         */
        httpResponse.setHeader(
                "Content-Security-Policy",
                "object-src 'none'; "
                        + "base-uri 'self'; "
                        + "form-action 'self'; "
                        + "frame-ancestors 'self'"
        );


        /*
         * Tell browsers how much referrer information
         * to send to other origins.
         */
        httpResponse.setHeader(
                "Referrer-Policy",
                "strict-origin-when-cross-origin"
        );


        /*
         * Disable browser features that this application
         * does not need.
         */
        httpResponse.setHeader(
                "Permissions-Policy",
                "camera=(), "
                        + "microphone=(), "
                        + "geolocation=()"
        );


        /*
         * Do not enable HSTS on ordinary HTTP localhost.
         * Enable it only when the application is served
         * over HTTPS.
         */
        if (httpRequest.isSecure()) {

            httpResponse.setHeader(
                    "Strict-Transport-Security",
                    "max-age=31536000; includeSubDomains"
            );
        }


        chain.doFilter(request, response);
    }
}