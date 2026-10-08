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

    <style>

        .project-filters {
            margin: 20px 0 25px;
            padding: 20px;
        }

        .filter-grid {
            display: grid;
            grid-template-columns:
                minmax(220px, 2fr)
                minmax(160px, 1fr)
                minmax(160px, 1fr)
                auto auto;

            gap: 14px;
            align-items: end;
        }

        .filter-group {
            display: flex;
            flex-direction: column;
            gap: 7px;
        }

        .filter-group label {
            font-size: 13px;
            font-weight: 600;
        }

        .filter-group input,
        .filter-group select {
            width: 100%;
            height: 44px;
            box-sizing: border-box;
        }

        .filter-button {
            height: 44px;
            min-width: 100px;
            box-sizing: border-box;
        }


        .project-card {
            height: auto !important;
            min-height: 0 !important;
            overflow: visible !important;
            box-sizing: border-box;
            align-self: start;
        }

        .projects-grid {
            align-items: start;
        }


        .status-form {
            margin-top: 18px;
            padding-top: 16px;
            border-top: 1px solid rgba(255,255,255,0.08);

            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;

            position: relative;
            z-index: 2;
        }

        .status-form label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            line-height: 1.4;
            margin: 0;
        }

        .status-form select {
            min-width: 155px;
            height: 40px;
            box-sizing: border-box;
        }

        .status-save-btn {
            height: 40px;
            padding: 0 16px;
        }


        .progress-section {
            margin-top: 18px;
        }

        .progress-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            margin-bottom: 8px;
        }

        .progress-label {
            font-size: 13px;
            font-weight: 600;
        }

        .progress-value {
            font-size: 13px;
            font-weight: 700;
            color: #c4b5fd;
        }

        .progress-track {
            width: 100%;
            height: 9px;
            border-radius: 999px;
            background: rgba(255,255,255,0.08);
            overflow: hidden;
        }

        .progress-fill {
            height: 100%;
            min-width: 0;
            border-radius: 999px;
            background: linear-gradient(
                90deg,
                #7c3aed,
                #a855f7
            );
            transition: width 0.25s ease;
        }

        .progress-empty {
            margin-top: 7px;
            font-size: 12px;
        }


        @media (max-width: 900px) {

            .filter-grid {
                grid-template-columns: 1fr 1fr;
            }

        }


        @media (max-width: 600px) {

            .filter-grid {
                grid-template-columns: 1fr;
            }

            .filter-button {
                width: 100%;
            }

            .status-form {
                align-items: stretch;
                flex-direction: column;
            }

            .status-form select,
            .status-save-btn {
                width: 100%;
            }

        }

    </style>

</head>


<body>

<div class="app-layout">


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

                <span>▣</span>
                Dashboard

            </a>


            <a class="sidebar-link"
               href="#projects">

                <span>◆</span>
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



    <main class="app-content page"
          id="top">


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



        <c:if test="${param.statusUpdated == 'true'}">

            <div class="message">

                Project status updated successfully.

            </div>

        </c:if>



        <c:if test="${not empty param.error}">

            <div class="message">

                <c:out value="${param.error}"/>

            </div>

        </c:if>


        <c:if test="${not empty error}">

            <div class="message">

                <c:out value="${error}"/>

            </div>

        </c:if>



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



        <div id="projects"
             class="dashboard-section">

            <h2>
                My Projects
            </h2>



            <div class="panel project-filters">

                <form method="get"
                      action="${pageContext.request.contextPath}/manager/dashboard">

                    <div class="filter-grid">


                        <div class="filter-group">

                            <label for="search">
                                Search
                            </label>

                            <input
                                type="text"
                                id="search"
                                name="search"
                                value="<c:out value='${search}'/>"
                                placeholder="Search by project name or description"
                            >

                        </div>



                        <div class="filter-group">

                            <label for="status">
                                Status
                            </label>

                            <select id="status"
                                    name="status">

                                <option value="ALL"
                                    ${selectedStatus == 'ALL' || empty selectedStatus ? 'selected' : ''}>
                                    All Statuses
                                </option>

                                <option value="PLANNED"
                                    ${selectedStatus == 'PLANNED' ? 'selected' : ''}>
                                    Planned
                                </option>

                                <option value="IN_PROGRESS"
                                    ${selectedStatus == 'IN_PROGRESS' ? 'selected' : ''}>
                                    In Progress
                                </option>

                                <option value="COMPLETED"
                                    ${selectedStatus == 'COMPLETED' ? 'selected' : ''}>
                                    Completed
                                </option>

                                <option value="ON_HOLD"
                                    ${selectedStatus == 'ON_HOLD' ? 'selected' : ''}>
                                    On Hold
                                </option>

                            </select>

                        </div>



                        <div class="filter-group">

                            <label for="priority">
                                Priority
                            </label>

                            <select id="priority"
                                    name="priority">

                                <option value="ALL"
                                    ${selectedPriority == 'ALL' || empty selectedPriority ? 'selected' : ''}>
                                    All Priorities
                                </option>

                                <option value="LOW"
                                    ${selectedPriority == 'LOW' ? 'selected' : ''}>
                                    Low
                                </option>

                                <option value="MEDIUM"
                                    ${selectedPriority == 'MEDIUM' ? 'selected' : ''}>
                                    Medium
                                </option>

                                <option value="HIGH"
                                    ${selectedPriority == 'HIGH' ? 'selected' : ''}>
                                    High
                                </option>

                            </select>

                        </div>



                        <button
                            type="submit"
                            class="btn btn-primary filter-button">

                            Search

                        </button>



                        <a
                            href="${pageContext.request.contextPath}/manager/dashboard"
                            class="btn btn-outline filter-button"
                            style="display:flex;align-items:center;justify-content:center;">

                            Clear

                        </a>

                    </div>

                </form>

            </div>



            <c:if test="${not empty projects}">

                <p class="muted"
                   style="margin-bottom:18px;">

                    Showing
                    <strong>
                        <c:out value="${projects.size()}"/>
                    </strong>
                    project(s)

                </p>

            </c:if>



            <c:choose>

                <c:when test="${empty projects}">

                    <div class="panel empty-state">

                        <c:choose>

                            <c:when test="${not empty search
                                           or selectedStatus != 'ALL'
                                           or selectedPriority != 'ALL'}">

                                <h3>
                                    No matching projects
                                </h3>

                                <p class="muted">

                                    Try changing your search or filters.

                                </p>

                            </c:when>

                            <c:otherwise>

                                <h3>
                                    No projects yet
                                </h3>

                                <p class="muted">

                                    Create your first project to get started.

                                </p>

                            </c:otherwise>

                        </c:choose>

                    </div>

                </c:when>


                <c:otherwise>

                    <div class="projects-grid">


                        <c:forEach var="p"
                                   items="${projects}">


                            <div class="project-card">


                                <p class="badge">

                                    <c:out value="${p.status}"/>

                                </p>



                                <h3>

                                    <a
                                        href="${pageContext.request.contextPath}/manager/project?id=${p.projectId}"
                                        style="text-decoration:none;"
                                    >

                                        <c:out value="${p.projectName}"/>

                                    </a>

                                </h3>



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



                                <div class="stat-line">

                                    <span class="muted">
                                        Priority
                                    </span>

                                    <span>
                                        <c:out value="${p.priority}"/>
                                    </span>

                                </div>



                                <div class="stat-line">

                                    <span class="muted">
                                        Start
                                    </span>

                                    <span>
                                        <c:out value="${p.startDate}"/>
                                    </span>

                                </div>



                                <div class="stat-line">

                                    <span class="muted">
                                        Due
                                    </span>

                                    <span>
                                        <c:out value="${p.endDate}"/>
                                    </span>

                                </div>



                                <div class="progress-section">

                                    <div class="progress-header">

                                        <span class="progress-label">
                                            Project Progress
                                        </span>

                                        <span class="progress-value">

                                            <c:out
                                                value="${empty projectProgress[p.projectId]
                                                        ? 0
                                                        : projectProgress[p.projectId]}"
                                            />%

                                        </span>

                                    </div>


                                    <div
                                        class="progress-track"
                                        role="progressbar"
                                        aria-valuemin="0"
                                        aria-valuemax="100"
                                        aria-valuenow="${empty projectProgress[p.projectId]
                                                        ? 0
                                                        : projectProgress[p.projectId]}"
                                    >

                                        <div
                                            class="progress-fill"
                                            style="width:${empty projectProgress[p.projectId]
                                                            ? 0
                                                            : projectProgress[p.projectId]}%;"
                                        ></div>

                                    </div>


                                    <c:if test="${empty projectProgress[p.projectId] || projectProgress[p.projectId] == 0}">

                                        <div class="muted progress-empty">
                                            No completed tasks yet.
                                        </div>

                                    </c:if>

                                </div>



                                <form
                                    class="status-form"
                                    method="post"
                                    action="${pageContext.request.contextPath}/manager/update-project-status"
                                >

                                    <input
                                        type="hidden"
                                        name="projectId"
                                        value="${p.projectId}"
                                    >


                                    <label for="status-${p.projectId}">
                                        Change Status
                                    </label>


                                    <select
                                        id="status-${p.projectId}"
                                        name="status"
                                    >

                                        <option value="PLANNED"
                                            ${p.status == 'PLANNED' ? 'selected' : ''}>
                                            Planned
                                        </option>

                                        <option value="IN_PROGRESS"
                                            ${p.status == 'IN_PROGRESS' ? 'selected' : ''}>
                                            In Progress
                                        </option>

                                        <option value="COMPLETED"
                                            ${p.status == 'COMPLETED' ? 'selected' : ''}>
                                            Completed
                                        </option>

                                        <option value="ON_HOLD"
                                            ${p.status == 'ON_HOLD' ? 'selected' : ''}>
                                            On Hold
                                        </option>

                                    </select>


                                    <button
                                        type="submit"
                                        class="btn btn-primary status-save-btn">

                                        Update

                                    </button>

                                </form>



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