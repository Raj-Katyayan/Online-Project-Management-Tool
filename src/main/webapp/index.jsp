<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>The JVM Crew | Online Project Management Tool</title>

    <link rel="stylesheet" href="css/style.css">

</head>

<body>

<header class="navbar">

    <div class="logo">
        <span class="logo-mark">J</span>
        <span>The JVM Crew</span>
    </div>

    <nav>
	    <a href="#home">Home</a>
        <a href="#features">Features</a>
        <a href="#about">About</a>
        <a href="#contact">Contact</a>
    </nav>

    <div class="nav-actions">

        <a class="btn btn-outline" href="login">
            Login
        </a>

        <a class="btn btn-primary" href="register">
            Get Started
        </a>

    </div>

</header>


<main>

    <!-- HERO -->

    <section id="home" class="hero" >

        <div>

            <p class="badge">
                JAVA • JSP • SERVLETS • MYSQL
            </p>

            <h1>
                Manage Projects.<br>
                <span>Build Together.</span>
            </h1>

            <p>
                Centralize projects, tasks, team members, deadlines,
                and progress in one application built with Java web
                technologies.
            </p>

            <div class="hero-buttons">

                <a class="btn btn-primary" href="login">
                    Start Managing
                </a>

                <a class="btn btn-outline" href="#features">
                    Explore Features
                </a>

            </div>

        </div>


        <div class="hero-card">

            <p class="badge">
                PROJECT
            </p>

            <h2>
                Online Project Management Tool
            </h2>

            <div class="progress-info">

                <span class="muted">
                    Project Progress
                </span>

                <span>
                    72%
                </span>

            </div>

            <div class="progress-bar">

                <div
                    class="progress"
                    style="width:72%">
                </div>

            </div>


            <div class="stat-grid">

                <div class="stat-card">

                    <strong>
                        24
                    </strong>

                    <div class="muted">
                        Tasks
                    </div>

                </div>


                <div class="stat-card">

                    <strong>
                        8
                    </strong>

                    <div class="muted">
                        Members
                    </div>

                </div>


                <div class="stat-card">

                    <strong>
                        12
                    </strong>

                    <div class="muted">
                        Completed
                    </div>

                </div>

            </div>

        </div>

    </section>


    <!-- FEATURES -->

    <section id="features" class="section">

        <div class="section-heading">

            <p class="badge">
                FEATURES
            </p>

            <h2>
                Everything your team needs
            </h2>

            <p class="muted">
                Plan, assign, track, and complete project work
                from one place.
            </p>

        </div>


        <div class="feature-grid">

            <div class="feature-card">

                <h3>
                    Project Management
                </h3>

                <p class="muted">
                    Create projects, deadlines, priority levels,
                    and project status.
                </p>

            </div>


            <div class="feature-card">

                <h3>
                    Task Management
                </h3>

                <p class="muted">
                    Create tasks, assign work, set priorities,
                    and update status.
                </p>

            </div>


            <div class="feature-card">

                <h3>
                    Team Collaboration
                </h3>

                <p class="muted">
                    Add team members to projects and keep
                    responsibilities visible.
                </p>

            </div>


            <div class="feature-card">

                <h3>
                    Progress Monitoring
                </h3>

                <p class="muted">
                    Give managers and members dashboards
                    tailored to their roles.
                </p>

            </div>

        </div>

    </section>


    <!-- ABOUT -->

    <section id="about" class="about">

        <div>

            <p class="badge">
                THE JVM CREW
            </p>

            <h2>
                Built as a real Java web application
            </h2>

            <p class="muted">
                This project demonstrates OOP, JDBC, Servlets,
                JSP, MySQL, authentication, sessions, role-based
                access, project management, and task workflows.
            </p>

        </div>


        <div>

            <div class="tech-stack">

                <span class="chip">
                    Java 26
                </span>

                <span class="chip">
                    JSP
                </span>

                <span class="chip">
                    Servlets
                </span>

                <span class="chip">
                    JDBC
                </span>

                <span class="chip">
                    MySQL
                </span>

                <span class="chip">
                    Tomcat
                </span>

                <span class="chip">
                    Maven
                </span>

            </div>

        </div>

    </section>


    <!-- CONTACT -->

    <section id="contact" class="contact-section">

        <div class="section-heading">

            <p class="badge">
                CONTACT
            </p>

            <h2>
                Get in touch
            </h2>

            <p class="muted">
                Have a question about The JVM Crew or the
                Online Project Management Tool? Contact us
                using the details below.
            </p>

        </div>


        <div class="contact-details">


            <!-- PHONE -->

            <a
                class="contact-card"
                href="tel:+918960904709">

                <div class="contact-icon">
                    📞
                </div>

                <div>

                    <span class="contact-label">
                        Phone
                    </span>

                    <strong>
                        +91 8960904709
                    </strong>

                </div>

            </a>


            <!-- EMAIL -->

            <a
                class="contact-card"
                href="mailto:YOUR_EMAIL@example.com">

                <div class="contact-icon">
                    ✉
                </div>

                <div>

                    <span class="contact-label">
                        Email
                    </span>

                    <strong>
                        rajkatyayan29@gmail.com
                    </strong>

                </div>

            </a>


        </div>

    </section>

</main>


<!-- FOOTER -->

<footer>

    <p>
        © 2026 The JVM Crew · Online Project Management Tool
    </p>

</footer>


<script src="js/script.js"></script>

</body>

</html>