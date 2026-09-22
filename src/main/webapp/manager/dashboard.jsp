<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Manager Dashboard | The JVM Crew
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="app-layout">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="sidebar-brand">

            <div class="sidebar-logo">
                J
            </div>

            <div>

                <strong>
                    The JVM Crew
                </strong>

                <span>
                    MANAGER
                </span>

            </div>

        </div>


        <nav class="sidebar-nav">

            <a class="sidebar-link active"
               href="#top">

                <span>⌂</span>
                Dashboard

            </a>


            <a class="sidebar-link"
               href="#projects">

                <span>▣</span>
                Projects

            </a>

        </nav>


        <div class="sidebar-bottom">

            <div class="sidebar-user">

                <strong>
                    <c:out value="${sessionScope.userName}"/>
                </strong>

                <span>
                    Project Manager
                </span>

            </div>


            <a class="sidebar-logout"
               href="${pageContext.request.contextPath}/logout">

                Logout

            </a>

        </div>

    </aside>


    <!-- MAIN -->

    <main class="app-content page"
          id="top">


        <!-- HEADER -->

        <div class="dashboard-header">

            <div>

                <p class="badge">
                    MANAGER WORKSPACE
                </p>

                <h1>

                    Welcome,
                    <c:out value="${sessionScope.userName}"/>

                </h1>

                <p class="muted">
                    Manage your projects and team workflow.
                </p>

            </div>


            <a class="btn btn-primary"
               href="${pageContext.request.contextPath}/manager/create-project">

                + Create Project

            </a>

        </div>


        <!-- ERROR -->

        <c:if test="${not empty error}">

            <div class="message">

                <c:out value="${error}"/>

            </div>

        </c:if>


        <!-- STATISTICS -->

        <div class="stat-grid dashboard-stats">


            <div class="stat-card">

                <strong>
                    <c:out value="${empty totalProjects ? 0 : totalProjects}"/>
                </strong>

                <div class="muted">
                    Total Projects
                </div>

            </div>


            <div class="stat-card">

                <strong>
                    <c:out value="${empty totalTasks ? 0 : totalTasks}"/>
                </strong>

                <div class="muted">
                    Total Tasks
                </div>

            </div>


            <div class="stat-card">

                <strong>
                    <c:out value="${empty inProgressTasks ? 0 : inProgressTasks}"/>
                </strong>

                <div class="muted">
                    In Progress
                </div>

            </div>


            <div class="stat-card">

                <strong>
                    <c:out value="${empty completedTasks ? 0 : completedTasks}"/>
                </strong>

                <div class="muted">
                    Completed
                </div>

            </div>


            <div class="stat-card">

                <strong>
                    <c:out value="${empty teamMembers ? 0 : teamMembers}"/>
                </strong>

                <div class="muted">
                    Team Members
                </div>

            </div>


        </div>


        <!-- PROJECTS -->

        <div id="projects"
             class="dashboard-section">

            <h2>
                My Projects
            </h2>


            <c:choose>

                <c:when test="${empty projects}">

                    <div class="panel empty-state">

                        <h3>
                            No projects yet
                        </h3>

                        <p class="muted">
                            Create your first project to get started.
                        </p>

                    </div>

                </c:when>


                <c:otherwise>

                    <div class="projects-grid">


                        <c:forEach var="p" items="${projects}">


                            <div class="project-card">


                                <!-- STATUS -->

                                <p class="badge">
                                    <c:out value="${p.status}"/>
                                </p>


                                <!-- PROJECT NAME -->

                                <h3>

                                    <a
                                        href="${pageContext.request.contextPath}/manager/project?id=${p.projectId}"
                                        style="text-decoration:none;"
                                    >

                                        <c:out value="${p.projectName}"/>

                                    </a>

                                </h3>


                                <!-- DESCRIPTION -->

                                <p class="muted">

                                    <c:choose>

                                        <c:when test="${not empty p.description}">

                                            <c:out value="${p.description}"/>

                                        </c:when>

                                        <c:otherwise>

                                            No description provided.

                                        </c:otherwise>

                                    </c:choose>

                                </p>


                                <!-- PRIORITY -->

                                <div class="stat-line">

                                    <span class="muted">
                                        Priority
                                    </span>

                                    <span>
                                        <c:out value="${p.priority}"/>
                                    </span>

                                </div>


                                <!-- START -->

                                <div class="stat-line">

                                    <span class="muted">
                                        Start
                                    </span>

                                    <span>
                                        <c:out value="${p.startDate}"/>
                                    </span>

                                </div>


                                <!-- DUE -->

                                <div class="stat-line">

                                    <span class="muted">
                                        Due
                                    </span>

                                    <span>
                                        <c:out value="${p.endDate}"/>
                                    </span>

                                </div>


                                <!-- VIEW PROJECT -->

                                <div style="margin-top:18px;">

                                    <a
                                        class="btn btn-outline"
                                        href="${pageContext.request.contextPath}/manager/project?id=${p.projectId}"
                                        style="display:inline-block;"
                                    >

                                        View Project →

                                    </a>

                                </div>


                            </div>


                        </c:forEach>


                    </div>

                </c:otherwise>

            </c:choose>

        </div>

    </main>

</div>

</body>

</html>