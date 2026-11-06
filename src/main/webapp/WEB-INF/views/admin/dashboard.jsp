<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Admin Dashboard | The JVM Crew
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
                    ADMIN
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
               href="#users">

                <span>♙</span>
                Users

            </a>


            <a class="sidebar-link"
               href="#activity">

                <span>◷</span>
                Activity

            </a>

        </nav>


        <div class="sidebar-bottom">

            <div class="sidebar-user">

                <strong>
                    <c:out value="${sessionScope.userName}"/>
                </strong>

                <span>
                    System Administrator
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
                    ADMIN CONTROL CENTER
                </p>

                <h1>

                    Welcome,
                    <c:out value="${sessionScope.userName}"/>

                </h1>

                <p class="muted">
                    System-level user, role, and activity management.
                </p>

            </div>

        </div>


        <!-- ERROR -->

        <c:if test="${not empty error}">

            <div class="message">

                <c:out value="${error}"/>

            </div>

        </c:if>


        <!-- =========================
             ACTIVITY
        ========================= -->

        <div id="activity"
             class="panel dashboard-section"
             style="margin-bottom:25px;">

            <div class="section-title-row">

                <div>

                    <h2>
                        Recent Activity
                    </h2>

                    <p class="muted">
                        Latest actions performed in the system.
                    </p>

                </div>

            </div>


            <c:choose>

                <c:when test="${empty activities}">

                    <p class="muted">
                        No activity recorded yet.
                    </p>

                </c:when>


                <c:otherwise>

                    <div class="activity-list">


                        <c:forEach var="activity"
                                   items="${activities}">


                            <div class="activity-item">


                                <div class="activity-main">


                                    <!-- ACTION -->

                                    <div class="activity-action">

                                        <c:choose>

                                            <c:when test="${not empty activity.action}">

                                                <c:out
                                                    value="${activity.action}"
                                                    escapeXml="true"
                                                />

                                            </c:when>

                                            <c:otherwise>

                                                ACTIVITY

                                            </c:otherwise>

                                        </c:choose>

                                    </div>


                                    <!-- DESCRIPTION -->

                                    <div class="activity-description">

                                        <c:choose>

                                            <c:when test="${not empty activity.description}">

                                                <c:out
                                                    value="${activity.description}"
                                                    escapeXml="true"
                                                />

                                            </c:when>

                                            <c:otherwise>

                                                No description available.

                                            </c:otherwise>

                                        </c:choose>

                                    </div>


                                    <!-- ACTIVITY META -->

                                    <div class="activity-meta">

                                        <span>

                                            By:

                                            <strong>

                                                <c:choose>

                                                    <c:when test="${not empty activity.userName}">

                                                        <c:out
                                                            value="${activity.userName}"
                                                            escapeXml="true"
                                                        />

                                                    </c:when>

                                                    <c:otherwise>

                                                        Unknown User

                                                    </c:otherwise>

                                                </c:choose>

                                            </strong>

                                        </span>


                                        <c:if test="${not empty activity.projectName}">

                                            <span>

                                                Project:

                                                <strong>

                                                    <c:out
                                                        value="${activity.projectName}"
                                                        escapeXml="true"
                                                    />

                                                </strong>

                                            </span>

                                        </c:if>

                                    </div>

                                </div>


                                <!-- TIME -->

                                <div class="activity-time">

                                    <c:if test="${not empty activity.createdAt}">

                                        <c:out
                                            value="${activity.createdAt}"
                                            escapeXml="true"
                                        />

                                    </c:if>

                                </div>


                            </div>


                        </c:forEach>


                    </div>

                </c:otherwise>

            </c:choose>

        </div>


        <!-- =========================
             USERS
        ========================= -->

        <div id="users"
             class="panel dashboard-section">

            <h2>
                Users
            </h2>


            <div class="table-wrapper">

                <table class="table">

                    <tr>

                        <th>ID</th>

                        <th>Name</th>

                        <th>Email</th>

                        <th>Role</th>

                        <th>Change Role</th>

                    </tr>


                    <c:if test="${not empty users}">

                        <c:forEach var="u"
                                   items="${users}">


                            <tr>

                                <!-- ID -->

                                <td>

                                    <c:out value="${u.userId}"/>

                                </td>


                                <!-- NAME -->

                                <td>

                                    <c:out
                                        value="${u.name}"
                                        escapeXml="true"
                                    />

                                </td>


                                <!-- EMAIL -->

                                <td>

                                    <c:out
                                        value="${u.email}"
                                        escapeXml="true"
                                    />

                                </td>


                                <!-- ROLE -->

                                <td>

                                    <c:out
                                        value="${u.role}"
                                        escapeXml="true"
                                    />

                                </td>


                                <!-- CHANGE ROLE -->

                                <td>

                                    <form
                                        action="${pageContext.request.contextPath}/admin/update-role"
                                        method="post"
                                        class="role-form"
                                    >

                                        <input
                                            type="hidden"
                                            name="userId"
                                            value="${u.userId}"
                                        >


                                        <select name="role">


                                            <option value="ADMIN">

                                                ADMIN

                                            </option>


                                            <option value="MANAGER">

                                                MANAGER

                                            </option>


                                            <option value="MEMBER">

                                                MEMBER

                                            </option>


                                        </select>


                                        <script>
                                            (function () {
                                                const form =
                                                    document.currentScript.closest("form");

                                                if (!form) return;

                                                const select =
                                                    form.querySelector("select[name='role']");

                                                if (!select) return;

                                                const currentRole =
                                                    "${u.role}";

                                                for (const option of select.options) {

                                                    if (option.value === currentRole) {
                                                        option.selected = true;
                                                        break;
                                                    }

                                                }

                                            })();
                                        </script>


                                        <button
                                            type="submit"
                                            class="btn btn-primary"
                                        >

                                            Save

                                        </button>

                                    </form>

                                </td>

                            </tr>


                        </c:forEach>

                    </c:if>


                </table>

            </div>

        </div>


    </main>

</div>

</body>

</html>