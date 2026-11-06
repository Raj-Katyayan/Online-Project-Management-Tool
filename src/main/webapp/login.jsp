<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | The JVM Crew</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="form-shell">
    <div class="form-card">
        <p class="badge">THE JVM CREW</p>
        <h1>Welcome Back</h1>
        <p class="muted">Login to your project workspace.</p>

        <c:if test="${not empty requestScope.error}">
            <div class="message">
                <c:out value="${requestScope.error}" />
            </div>
        </c:if>

        <c:if test="${param.registered eq 'true'}">
            <div class="message">Account created successfully. You can now log in.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" placeholder="Enter your email" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" placeholder="Enter your password" required>
            </div>
            <button class="btn btn-primary" style="width:100%" type="submit">Login</button>
        </form>

        <p class="muted" style="text-align:center;margin-top:20px">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register" style="color:#a78bfa">Create one</a>
        </p>
    </div>
</div>
</body>
</html>
