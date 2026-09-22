<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>Login | The JVM Crew</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="form-shell"><div class="form-card">
<p class="badge">THE JVM CREW</p><h1>Welcome Back</h1><p class="muted">Login to your project workspace.</p>
<% String error=(String)request.getAttribute("error"); String registered=request.getParameter("registered"); if(error!=null){ %><div class="message"><%= error %></div><% } if("true".equals(registered)){ %><div class="message">Account created successfully. You can now log in.</div><% } %>
<form action="login" method="post">
<div class="form-group"><label>Email</label><input type="email" name="email" placeholder="Enter your email" required></div>
<div class="form-group"><label>Password</label><input type="password" name="password" placeholder="Enter your password" required></div>
<button class="btn btn-primary" style="width:100%">Login</button>
</form><p class="muted" style="text-align:center;margin-top:20px">Don't have an account? <a href="register" style="color:#a78bfa">Create one</a></p>
</div></div></body></html>
