<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.jvmcrew.model.Task" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Member Dashboard | The JVM Crew
    </title>

    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/css/style.css">

</head>

<body>

<div class="app-layout">

    <!-- =========================
         SIDEBAR
    ========================= -->

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
                    MEMBER
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
               href="#tasks">

                <span>✓</span>
                My Tasks

            </a>

        </nav>


        <div class="sidebar-bottom">

            <div class="sidebar-user">

                <strong>
                    <%=session.getAttribute("userName")%>
                </strong>

                <span>
                    Team Member
                </span>

            </div>


            <a class="sidebar-logout"
               href="<%=request.getContextPath()%>/logout">

                Logout

            </a>

        </div>

    </aside>



    <!-- =========================
         MAIN CONTENT
    ========================= -->

    <main class="app-content page"
          id="top">


        <!-- HEADER -->

        <div class="dashboard-header">

            <div>

                <p class="badge">
                    MEMBER WORKSPACE
                </p>

                <h1>
                    Welcome,
                    <%=session.getAttribute("userName")%>
                </h1>

                <p class="muted">
                    Your assigned project work.
                </p>

            </div>

        </div>



        <!-- ERROR -->

        <%
            String error =
                    (String)request.getAttribute("error");

            if(error != null){
        %>

            <div class="message">

                <%=error%>

            </div>

        <%
            }
        %>



        <!-- TASKS -->

        <div id="tasks"
             class="dashboard-section">

            <h2>
                My Tasks
            </h2>


            <%
                List<Task> tasks =
                        (List<Task>)
                        request.getAttribute("tasks");


                if(tasks == null || tasks.isEmpty()){
            %>


                <div class="panel empty-state">

                    <h3>
                        No assigned tasks yet
                    </h3>

                    <p class="muted">
                        Your manager will assign tasks to you.
                    </p>

                </div>


            <%
                } else {
            %>


                <div class="task-grid">


                <%
                    for(Task t : tasks){
                %>


                    <div class="card task-card">


                        <p class="badge">
                            <%=t.getProjectName()%>
                        </p>


                        <h3>
                            <%=t.getTitle()%>
                        </h3>


                        <p class="muted">

                            <%=t.getDescription() == null
                                ? "No description."
                                : t.getDescription()%>

                        </p>


                        <div class="task-meta">

                            <span>
                                Priority:
                                <%=t.getPriority()%>
                            </span>

                            <span>
                                Due:
                                <%=t.getDueDate()%>
                            </span>

                            <span>
                                Status:
                                <%=t.getStatus()%>
                            </span>

                        </div>


                        <!-- STATUS UPDATE -->

                        <div class="task-actions">

                            <form
                                action="<%=request.getContextPath()%>/member/update-task"
                                method="post"
                            >

                                <input
                                    type="hidden"
                                    name="taskId"
                                    value="<%=t.getTaskId()%>"
                                >


                                <select name="status">

                                    <option
                                        <%= "TODO".equals(t.getStatus())
                                            ? "selected"
                                            : "" %>
                                    >
                                        TODO
                                    </option>


                                    <option
                                        <%= "IN_PROGRESS".equals(t.getStatus())
                                            ? "selected"
                                            : "" %>
                                    >
                                        IN_PROGRESS
                                    </option>


                                    <option
                                        <%= "COMPLETED".equals(t.getStatus())
                                            ? "selected"
                                            : "" %>
                                    >
                                        COMPLETED
                                    </option>


                                    <option
                                        <%= "BLOCKED".equals(t.getStatus())
                                            ? "selected"
                                            : "" %>
                                    >
                                        BLOCKED
                                    </option>

                                </select>


                                <button
                                    type="submit"
                                    class="btn btn-primary"
                                    style="margin-top:8px"
                                >

                                    Update Status

                                </button>

                            </form>

                        </div>

                    </div>


                <%
                    }
                %>


                </div>


            <%
                }
            %>

        </div>

    </main>

</div>

</body>
</html>