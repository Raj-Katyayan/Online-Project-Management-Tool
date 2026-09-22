<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Project | The JVM Crew</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>

        /* =========================
           PROJECT HEADER BUTTONS
        ========================= */

        .project-action-btn {
            width: 176px;
            height: 60px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            box-sizing: border-box;
            white-space: nowrap;
        }

        .project-action-form {
            margin: 0;
        }


        /* =========================
           MOBILE
        ========================= */

        @media (max-width: 800px) {

            .project-action-btn {
                width: 100%;
            }

            .project-action-form {
                width: 100%;
            }

        }

    </style>

</head>


<body>

<div class="page">

    <!-- BACK TO DASHBOARD -->

    <a href="${pageContext.request.contextPath}/manager/dashboard"
       style="color:#a78bfa;text-decoration:none">

        ← Back to Dashboard

    </a>


    <!-- PROJECT DELETED MESSAGE -->

    <c:if test="${param.deleted == 'project'}">

        <div class="message"
             style="margin-top:20px">

            Project deleted successfully.

        </div>

    </c:if>


    <!-- TASK DELETED MESSAGE -->

    <c:if test="${param.deleted == 'true'}">

        <div class="message"
             style="margin-top:20px">

            Task deleted successfully.

        </div>

    </c:if>


    <!-- ERROR MESSAGE -->

    <c:if test="${not empty param.error}">

        <div class="message"
             style="margin-top:20px">

            <c:out value="${param.error}"/>

        </div>

    </c:if>


    <c:if test="${not empty project}">


        <!-- =========================
             PROJECT HEADER
        ========================= -->

        <div class="dashboard-header"
             style="margin-top:25px">

            <div>

                <p class="badge">

                    <c:out value="${project.status}"/>

                </p>


                <h1>

                    <c:out value="${project.projectName}"/>

                </h1>


                <p class="muted">

                    <c:choose>

                        <c:when test="${not empty project.description}">

                            <c:out value="${project.description}"/>

                        </c:when>


                        <c:otherwise>

                            No description provided.

                        </c:otherwise>

                    </c:choose>

                </p>

            </div>


            <!-- PROJECT ACTIONS -->

            <div style="
                display:flex;
                flex-wrap:wrap;
                justify-content:flex-end;
                align-items:center;
                gap:12px;
            ">


                <!-- ADD MEMBER -->

                <a
                    class="btn btn-outline project-action-btn"
                    href="${pageContext.request.contextPath}/manager/add-member?projectId=${project.projectId}"
                >

                    + Add Member

                </a>


                <!-- CREATE TASK -->

                <a
                    class="btn btn-primary project-action-btn"
                    href="${pageContext.request.contextPath}/manager/create-task?projectId=${project.projectId}"
                >

                    + Create Task

                </a>


                <!-- DELETE PROJECT -->

                <form
                    class="project-action-form"
                    method="post"
                    action="${pageContext.request.contextPath}/manager/delete-project"
                    onsubmit="return confirm('Are you sure you want to permanently delete this project and all of its tasks and members?');"
                >

                    <input
                        type="hidden"
                        name="projectId"
                        value="${project.projectId}"
                    >


                    <button
                        type="submit"
                        class="btn btn-danger project-action-btn"
                    >

                        Delete Project

                    </button>

                </form>


            </div>

        </div>


        <!-- =========================
             PROJECT OVERVIEW + MEMBERS
        ========================= -->

        <div class="feature-grid"
             style="grid-template-columns:1.2fr .8fr">


            <!-- PROJECT OVERVIEW -->

            <div class="panel">

                <h2>
                    Project Overview
                </h2>


                <div class="detail-row">

                    <span class="detail-label">
                        Priority
                    </span>

                    <span>

                        <c:out value="${project.priority}"/>

                    </span>

                </div>


                <div class="detail-row">

                    <span class="detail-label">
                        Status
                    </span>

                    <span>

                        <c:out value="${project.status}"/>

                    </span>

                </div>


                <div class="detail-row">

                    <span class="detail-label">
                        Start Date
                    </span>

                    <span>

                        <c:out value="${project.startDate}"/>

                    </span>

                </div>


                <div class="detail-row">

                    <span class="detail-label">
                        End Date
                    </span>

                    <span>

                        <c:out value="${project.endDate}"/>

                    </span>

                </div>

            </div>


            <!-- TEAM MEMBERS -->

            <div class="panel">

                <h2>
                    Team Members
                </h2>


                <div class="member-list">


                    <c:choose>

                        <c:when test="${empty members}">

                            <p class="muted">

                                No team members added yet.

                            </p>

                        </c:when>


                        <c:otherwise>

                            <c:forEach
                                var="u"
                                items="${members}"
                            >

                                <div class="member-item">


                                    <div>

                                        <strong>

                                            <c:out value="${u.name}"/>

                                        </strong>


                                        <div class="member-email">

                                            <c:out value="${u.email}"/>

                                        </div>

                                    </div>


                                    <div style="
                                        display:flex;
                                        align-items:center;
                                        gap:12px;
                                    ">

                                        <span style="
                                            color:#a78bfa;
                                        ">

                                            <c:out value="${u.role}"/>

                                        </span>


                                        <!-- KICK MEMBER -->

                                        <form
                                            method="post"
                                            action="${pageContext.request.contextPath}/manager/kick-member"
                                            style="margin:0;"
                                            onsubmit="return confirm('Remove this member from the project?');"
                                        >

                                            <input
                                                type="hidden"
                                                name="projectId"
                                                value="${project.projectId}"
                                            >


                                            <input
                                                type="hidden"
                                                name="userId"
                                                value="${u.userId}"
                                            >


                                            <button
                                                type="submit"
                                                class="btn btn-danger"
                                                style="
                                                    padding:6px 10px;
                                                    font-size:12px;
                                                "
                                            >

                                                Kick

                                            </button>

                                        </form>

                                    </div>

                                </div>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                </div>

            </div>

        </div>


        <!-- =========================
             TASKS
        ========================= -->

        <div
            class="panel"
            style="margin-top:25px"
        >

            <h2>
                Tasks
            </h2>


            <c:choose>

                <c:when test="${empty tasks}">

                    <p class="muted">

                        No tasks created yet.

                    </p>

                </c:when>


                <c:otherwise>

                    <div class="task-grid">


                        <c:forEach
                            var="t"
                            items="${tasks}"
                        >


                            <c:set
                                var="ids"
                                value="${assigneesByTask[t.taskId]}"
                            />


                            <div class="card task-card">


                                <!-- TASK STATUS -->

                                <p class="badge">

                                    <c:out value="${t.status}"/>

                                </p>


                                <!-- TASK TITLE -->

                                <h3>

                                    <c:out value="${t.title}"/>

                                </h3>


                                <!-- TASK DESCRIPTION -->

                                <p class="muted">

                                    <c:choose>

                                        <c:when test="${not empty t.description}">

                                            <c:out value="${t.description}"/>

                                        </c:when>


                                        <c:otherwise>

                                            No description.

                                        </c:otherwise>

                                    </c:choose>

                                </p>


                                <!-- TASK META -->

                                <div class="task-meta">

                                    <span>

                                        Priority:

                                        <c:out value="${t.priority}"/>

                                    </span>


                                    <span>

                                        Due:

                                        <c:out value="${t.dueDate}"/>

                                    </span>

                                </div>


                                <!-- ASSIGNED MEMBERS -->

                                <div style="
                                    margin-top:15px;
                                    padding:12px 14px;
                                    background:#211a2d;
                                    border:1px solid #302744;
                                    border-radius:10px;
                                ">


                                    <div style="
                                        color:#8f879a;
                                        font-size:13px;
                                        margin-bottom:10px;
                                    ">

                                        Assigned To

                                    </div>


                                    <c:set
                                        var="foundAssignee"
                                        value="false"
                                    />


                                    <c:if
                                        test="${not empty ids && not empty members}"
                                    >

                                        <c:forEach
                                            var="assignedUserId"
                                            items="${ids}"
                                        >

                                            <c:forEach
                                                var="u"
                                                items="${members}"
                                            >

                                                <c:if
                                                    test="${u.userId == assignedUserId}"
                                                >

                                                    <c:set
                                                        var="foundAssignee"
                                                        value="true"
                                                    />


                                                    <div style="
                                                        display:flex;
                                                        justify-content:space-between;
                                                        align-items:center;
                                                        gap:10px;
                                                        margin-bottom:8px;
                                                    ">


                                                        <span style="
                                                            color:#a78bfa;
                                                            font-weight:700;
                                                        ">

                                                            <c:out value="${u.name}"/>

                                                        </span>


                                                        <!-- UNASSIGN -->

                                                        <form
                                                            method="post"
                                                            action="${pageContext.request.contextPath}/manager/unassign-task"
                                                            style="margin:0;"
                                                        >

                                                            <input
                                                                type="hidden"
                                                                name="projectId"
                                                                value="${project.projectId}"
                                                            >


                                                            <input
                                                                type="hidden"
                                                                name="taskId"
                                                                value="${t.taskId}"
                                                            >


                                                            <input
                                                                type="hidden"
                                                                name="userId"
                                                                value="${u.userId}"
                                                            >


                                                            <button
                                                                type="submit"
                                                                class="btn btn-danger"
                                                                style="
                                                                    padding:6px 10px;
                                                                    font-size:12px;
                                                                "
                                                            >

                                                                Unassign

                                                            </button>

                                                        </form>

                                                    </div>

                                                </c:if>

                                            </c:forEach>

                                        </c:forEach>

                                    </c:if>


                                    <c:if test="${not foundAssignee}">

                                        <span class="muted">

                                            Not assigned

                                        </span>

                                    </c:if>

                                </div>


                                <!-- ASSIGN TASK -->

                                <div class="task-actions">

                                    <form
                                        class="inline-form"
                                        action="${pageContext.request.contextPath}/manager/assign-task"
                                        method="post"
                                    >


                                        <input
                                            type="hidden"
                                            name="projectId"
                                            value="${project.projectId}"
                                        >


                                        <input
                                            type="hidden"
                                            name="taskId"
                                            value="${t.taskId}"
                                        >


                                        <select name="userId">

                                            <option value="">

                                                Assign member...

                                            </option>


                                            <c:if test="${not empty members}">

                                                <c:forEach
                                                    var="u"
                                                    items="${members}"
                                                >

                                                    <option
                                                        value="${u.userId}"
                                                    >

                                                        <c:out value="${u.name}"/>

                                                    </option>

                                                </c:forEach>

                                            </c:if>

                                        </select>


                                        <button
                                            type="submit"
                                            class="btn btn-primary"
                                            style="margin-top:8px"
                                        >

                                            Assign

                                        </button>

                                    </form>

                                </div>


                                <!-- DELETE TASK -->

                                <div style="
                                    margin-top:12px;
                                ">

                                    <form
                                        method="post"
                                        action="${pageContext.request.contextPath}/manager/delete-task"
                                        style="margin:0;"
                                        onsubmit="return confirm('Are you sure you want to permanently delete this task?');"
                                    >

                                        <input
                                            type="hidden"
                                            name="projectId"
                                            value="${project.projectId}"
                                        >


                                        <input
                                            type="hidden"
                                            name="taskId"
                                            value="${t.taskId}"
                                        >


                                        <button
                                            type="submit"
                                            class="btn btn-danger"
                                            style="width:100%;"
                                        >

                                            Delete Task

                                        </button>

                                    </form>

                                </div>


                            </div>

                        </c:forEach>

                    </div>

                </c:otherwise>

            </c:choose>

        </div>

    </c:if>

</div>

</body>

</html>