package com.jvmcrew.controller;

import com.jvmcrew.dao.UserDAO;
import com.jvmcrew.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/login.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");


        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");


        /*
         * =========================
         * REQUIRED FIELD CHECK
         * =========================
         */

        if (email == null
                || password == null
                || email.isBlank()
                || password.isBlank()) {

            request.setAttribute(
                    "error",
                    "Email and password are required."
            );

            forwardToLogin(request, response);
            return;
        }


        email = email.trim().toLowerCase();


        /*
         * =========================
         * EMAIL VALIDATION
         * =========================
         */

        if (!EMAIL_PATTERN.matcher(email).matches()) {

            request.setAttribute(
                    "error",
                    "Please enter a valid email address."
            );

            forwardToLogin(request, response);
            return;
        }


        /*
         * =========================
         * DATABASE LOGIN
         * =========================
         */

        try {

            User user =
                    userDAO.loginUser(
                            email,
                            password
                    );


            /*
             * Never reveal whether the email
             * exists. Keep credentials errors generic.
             */

            if (user == null) {

                request.setAttribute(
                        "error",
                        "Invalid email or password."
                );

                forwardToLogin(request, response);
                return;
            }


            /*
             * =========================
             * SESSION RENEWAL
             * =========================
             *
             * Invalidate any existing session first,
             * then create a fresh session after login.
             */

            HttpSession oldSession =
                    request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }


            HttpSession session =
                    request.getSession(true);


            session.setAttribute(
                    "user",
                    user
            );

            session.setAttribute(
                    "userId",
                    user.getUserId()
            );

            session.setAttribute(
                    "userName",
                    user.getName()
            );

            session.setAttribute(
                    "userRole",
                    user.getRole()
            );


            /*
             * =========================
             * ROLE REDIRECTION
             * =========================
             */

            switch (user.getRole()) {

                case "ADMIN" ->

                        response.sendRedirect(
                                request.getContextPath()
                                + "/admin/dashboard"
                        );


                case "MANAGER" ->

                        response.sendRedirect(
                                request.getContextPath()
                                + "/manager/dashboard"
                        );


                default ->

                        response.sendRedirect(
                                request.getContextPath()
                                + "/member/dashboard"
                        );
            }


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "We could not connect to the database right now. Please try again."
            );

            forwardToLogin(request, response);
        }
    }


    private void forwardToLogin(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/login.jsp"
        ).forward(request, response);
    }
}