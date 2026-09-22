<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>Create Account | The JVM Crew</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="form-shell"><div class="form-card">
<p class="badge">THE JVM CREW</p><h1>Create Account</h1><p class="muted">Join your project management workspace.</p>
<% String error=(String)request.getAttribute("error"); if(error!=null){ %><div class="message"><%= error %></div><% } %>
<form action="register" method="post">
<div class="form-group"><label>Full Name</label><input type="text" name="name" placeholder="Enter your name" required></div>
<div class="form-group"><label>Email</label><input type="email" name="email" placeholder="Enter your email" required></div>
<div class="form-group"><label>Password</label><input type="password" name="password" placeholder="At least 8 characters" minlength="8" required></div>
<button class="btn btn-primary" style="width:100%">Create Account</button>
</form><p class="muted" style="text-align:center;margin-top:20px">Already have an account? <a href="login" style="color:#a78bfa">Login</a></p>
</div></div></body></html>
