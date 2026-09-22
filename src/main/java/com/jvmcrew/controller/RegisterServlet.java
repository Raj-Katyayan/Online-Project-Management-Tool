package com.jvmcrew.controller;

import com.jvmcrew.dao.UserDAO;
import com.jvmcrew.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
                "/register.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");


        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");


        /*
         * =========================
         * REQUIRED FIELD CHECK
         * =========================
         */

        if (name == null
                || email == null
                || password == null
                || name.isBlank()
                || email.isBlank()
                || password.isBlank()) {

            request.setAttribute(
                    "error",
                    "All fields are required."
            );

            forwardToRegister(request, response);
            return;
        }


        name = name.trim();
        email = email.trim().toLowerCase();


        /*
         * =========================
         * NAME VALIDATION
         * =========================
         */

        if (name.length() < 2) {

            request.setAttribute(
                    "error",
                    "Name must contain at least 2 characters."
            );

            forwardToRegister(request, response);
            return;
        }


        if (name.length() > 100) {

            request.setAttribute(
                    "error",
                    "Name must not exceed 100 characters."
            );

            forwardToRegister(request, response);
            return;
        }


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

            forwardToRegister(request, response);
            return;
        }


        /*
         * =========================
         * PASSWORD VALIDATION
         * =========================
         */

        if (password.length() < 8) {

            request.setAttribute(
                    "error",
                    "Password must contain at least 8 characters."
            );

            forwardToRegister(request, response);
            return;
        }


        if (password.length() > 72) {

            request.setAttribute(
                    "error",
                    "Password must not exceed 72 characters."
            );

            forwardToRegister(request, response);
            return;
        }


        /*
         * =========================
         * DATABASE
         * =========================
         */

        try {

            if (userDAO.emailExists(email)) {

                request.setAttribute(
                        "error",
                        "This email is already registered. Please use another email or log in."
                );

                forwardToRegister(request, response);
                return;
            }


            User user =
                    new User(
                            name,
                            email,
                            password,
                            "MEMBER"
                    );


            if (userDAO.registerUser(user)) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/login?registered=true"
                );

                return;
            }


            request.setAttribute(
                    "error",
                    "Registration could not be completed. Please try again."
            );

            forwardToRegister(request, response);


        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "We could not complete your registration right now. Please try again."
            );

            forwardToRegister(request, response);
        }
    }


    private void forwardToRegister(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/register.jsp"
        ).forward(request, response);
    }
}