<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | The JVM Crew</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="form-shell">
    <div class="form-card">
        <p class="badge">THE JVM CREW</p>
        <h1>Create Account</h1>
        <p class="muted">Join your project management workspace.</p>

        <c:if test="${not empty requestScope.error}">
            <div class="message">
                <c:out value="${requestScope.error}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="name">Full Name</label>
                <input id="name" type="text" name="name" placeholder="Enter your name" required>
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" placeholder="Enter your email" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input id="password" type="password" name="password" placeholder="At least 8 characters" minlength="8" required>
            </div>
            <button class="btn btn-primary" style="width:100%" type="submit">Create Account</button>
        </form>

        <p class="muted" style="text-align:center;margin-top:20px">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login" style="color:#a78bfa">Login</a>
        </p>
    </div>
</div>
</body>
</html>
