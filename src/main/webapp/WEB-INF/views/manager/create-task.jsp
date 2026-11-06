<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Task | The JVM Crew</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="form-shell">
    <div class="form-card">
        <p class="badge">CREATE TASK</p>
        <h1>Create Task</h1>

        <c:if test="${not empty requestScope.error}">
            <div class="message"><c:out value="${requestScope.error}" /></div>
        </c:if>

        <c:choose>
            <c:when test="${not empty requestScope.project}">
                <p class="muted">Project: <strong><c:out value="${project.projectName}" /></strong></p>

                <form action="${pageContext.request.contextPath}/manager/create-task" method="post">
                    <input type="hidden" name="projectId" value="<c:out value='${project.projectId}' />">

                    <div class="form-group">
                        <label for="title">Task Title</label>
                        <input id="title" name="title" maxlength="150" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" maxlength="1000"></textarea>
                    </div>

                    <div class="form-group">
                        <label for="priority">Priority</label>
                        <select id="priority" name="priority">
                            <option value="LOW">LOW</option>
                            <option value="MEDIUM" selected>MEDIUM</option>
                            <option value="HIGH">HIGH</option>
                        </select>
                    </div>

                    <div class="grid-2">
                        <div class="form-group">
                            <label for="startDate">Start Date</label>
                            <input id="startDate" type="date" name="startDate" required>
                        </div>
                        <div class="form-group">
                            <label for="dueDate">Due Date</label>
                            <input id="dueDate" type="date" name="dueDate" required>
                        </div>
                    </div>

                    <div style="display:flex;justify-content:flex-end;gap:10px">
                        <a class="btn btn-outline" href="${pageContext.request.contextPath}/manager/project?id=${project.projectId}">Cancel</a>
                        <button class="btn btn-primary" type="submit">Create Task</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <p class="muted">Project information could not be loaded. Return to your dashboard and open the project again.</p>
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/manager/dashboard">Back to Dashboard</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
