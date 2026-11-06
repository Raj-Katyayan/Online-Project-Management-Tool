<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Members | The JVM Crew</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <c:choose>
        <c:when test="${not empty requestScope.project}">
            <a href="${pageContext.request.contextPath}/manager/project?id=${project.projectId}"
               style="color:#a78bfa;text-decoration:none">← Back to Project</a>

            <div class="form-card" style="max-width:850px;margin:30px auto 0">
                <p class="badge">ADD TEAM MEMBER</p>
                <h1>Add Members</h1>
                <p class="muted">Project: <strong><c:out value="${project.projectName}" /></strong></p>

                <c:if test="${not empty requestScope.error}">
                    <div class="message"><c:out value="${requestScope.error}" /></div>
                </c:if>

                <c:choose>
                    <c:when test="${empty requestScope.members}">
                        <p class="muted">No eligible team members are available.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="member" items="${requestScope.members}">
                            <c:set var="memberAlreadyAdded" value="${false}" />
                            <c:forEach var="existingId" items="${requestScope.existingMemberIds}">
                                <c:if test="${existingId eq member.userId}">
                                    <c:set var="memberAlreadyAdded" value="${true}" />
                                </c:if>
                            </c:forEach>

                            <div class="member-item" style="margin-bottom:12px">
                                <div>
                                    <strong><c:out value="${member.name}" /></strong>
                                    <div class="muted"><c:out value="${member.email}" /></div>
                                </div>

                                <c:choose>
                                    <c:when test="${memberAlreadyAdded}">
                                        <span style="color:#70e0a0">✓ Added</span>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/manager/add-member" method="post">
                                            <input type="hidden" name="projectId" value="<c:out value='${project.projectId}' />">
                                            <input type="hidden" name="userId" value="<c:out value='${member.userId}' />">
                                            <button class="btn btn-primary" type="submit">Add</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-card" style="max-width:650px;margin:30px auto 0">
                <h1>Project unavailable</h1>
                <p class="muted">The project details could not be loaded.</p>
                <c:if test="${not empty requestScope.error}">
                    <div class="message"><c:out value="${requestScope.error}" /></div>
                </c:if>
                <a class="btn btn-outline" href="${pageContext.request.contextPath}/manager/dashboard">Back to Dashboard</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
