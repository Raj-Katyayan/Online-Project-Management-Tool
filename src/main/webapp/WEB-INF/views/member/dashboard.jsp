<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard | The JVM Crew</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-layout">
    <aside class="sidebar">
        <div class="sidebar-brand">
            <div class="sidebar-logo">J</div>
            <div>
                <strong>The JVM Crew</strong>
                <span>MEMBER</span>
            </div>
        </div>

        <nav class="sidebar-nav">
            <a class="sidebar-link active" href="#top"><span>⌂</span>Dashboard</a>
            <a class="sidebar-link" href="#tasks"><span>✓</span>My Tasks</a>
        </nav>

        <div class="sidebar-bottom">
            <div class="sidebar-user">
                <strong><c:out value="${sessionScope.userName}" /></strong>
                <span>Team Member</span>
            </div>
            <a class="sidebar-logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </aside>

    <main class="app-content page" id="top">
        <div class="dashboard-header">
            <div>
                <p class="badge">MEMBER WORKSPACE</p>
                <h1>Welcome, <c:out value="${sessionScope.userName}" /></h1>
                <p class="muted">Your assigned project work.</p>
            </div>
        </div>

        <c:if test="${not empty requestScope.error}">
            <div class="message">
                <c:out value="${requestScope.error}" />
            </div>
        </c:if>

        <div id="tasks" class="dashboard-section">
            <h2>My Tasks</h2>

            <c:choose>
                <c:when test="${empty requestScope.tasks}">
                    <div class="panel empty-state">
                        <h3>No assigned tasks yet</h3>
                        <p class="muted">Your manager will assign tasks to you.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="task-grid">
                        <c:forEach var="task" items="${requestScope.tasks}">
                            <div class="card task-card">
                                <p class="badge"><c:out value="${task.projectName}" /></p>
                                <h3><c:out value="${task.title}" /></h3>

                                <p class="muted">
                                    <c:choose>
                                        <c:when test="${empty task.description}">No description.</c:when>
                                        <c:otherwise><c:out value="${task.description}" /></c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="task-meta">
                                    <span>Priority: <c:out value="${task.priority}" /></span>
                                    <span>Due: <c:out value="${task.dueDate}" /></span>
                                    <span>Status: <c:out value="${task.status}" /></span>
                                </div>

                                <div class="task-actions">
                                    <form action="${pageContext.request.contextPath}/member/update-task" method="post">
                                        <input type="hidden" name="taskId" value="<c:out value='${task.taskId}' />">
                                        <label class="visually-hidden" for="status-${task.taskId}">Update task status</label>
                                        <select id="status-${task.taskId}" name="status">
                                            <c:choose>
                                                <c:when test="${task.status eq 'TODO'}">
                                                    <option value="TODO" selected>TODO</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="TODO">TODO</option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${task.status eq 'IN_PROGRESS'}">
                                                    <option value="IN_PROGRESS" selected>IN_PROGRESS</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="IN_PROGRESS">IN_PROGRESS</option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${task.status eq 'COMPLETED'}">
                                                    <option value="COMPLETED" selected>COMPLETED</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="COMPLETED">COMPLETED</option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${task.status eq 'BLOCKED'}">
                                                    <option value="BLOCKED" selected>BLOCKED</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="BLOCKED">BLOCKED</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                        <button type="submit" class="btn btn-primary" style="margin-top:8px">
                                            Update Status
                                        </button>
                                    </form>
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
