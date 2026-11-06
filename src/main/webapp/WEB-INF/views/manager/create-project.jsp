<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Create Project | The JVM Crew
    </title>

    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/css/style.css">

    <style>

        /* =========================
           DATE FIELD FIX
        ========================= */

        .date-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px;
            width: 100%;
        }

        .date-grid .form-group {
            min-width: 0;
        }

        .date-grid input[type="date"] {
            width: 100%;
            min-width: 0;
            box-sizing: border-box;
            display: block;
        }

        /* =========================
           RESPONSIVE
        ========================= */

        @media (max-width: 700px) {

            .date-grid {
                grid-template-columns: 1fr;
                gap: 0;
            }

        }

    </style>

</head>


<body>


<div class="form-shell">

    <div class="form-card">

        <p class="badge">
            MANAGER WORKSPACE
        </p>


        <h1>
            Create Project
        </h1>


        <p class="muted">
            Set up a new project for your team.
        </p>


        <!-- ERROR MESSAGE -->

        <c:if test="${not empty error}">

            <div class="message">

                <c:out value="${error}"/>

            </div>

        </c:if>


        <!-- CREATE PROJECT FORM -->

        <form
            action="<%=request.getContextPath()%>/manager/create-project"
            method="post"
        >


            <!-- PROJECT NAME -->

            <div class="form-group">

                <label for="projectName">
                    Project Name
                </label>

                <input
                    id="projectName"
                    type="text"
                    name="projectName"
                    required
                    minlength="2"
                    maxlength="150"
                    autocomplete="off"
                >

            </div>


            <!-- DESCRIPTION -->

            <div class="form-group">

                <label for="description">
                    Description
                </label>

                <textarea
                    id="description"
                    name="description"
                    maxlength="1000"
                ></textarea>

            </div>


            <!-- DATES -->

            <div class="date-grid">


                <div class="form-group">

                    <label for="startDate">
                        Start Date
                    </label>

                    <input
                        id="startDate"
                        type="date"
                        name="startDate"
                        required
                    >

                </div>


                <div class="form-group">

                    <label for="endDate">
                        End Date
                    </label>

                    <input
                        id="endDate"
                        type="date"
                        name="endDate"
                        required
                    >

                </div>


            </div>


            <!-- PRIORITY -->

            <div class="form-group">

                <label for="priority">
                    Priority
                </label>

                <select
                    id="priority"
                    name="priority"
                >

                    <option value="LOW">
                        LOW
                    </option>

                    <option value="MEDIUM" selected>
                        MEDIUM
                    </option>

                    <option value="HIGH">
                        HIGH
                    </option>

                </select>

            </div>


            <!-- ACTIONS -->

            <div
                style="
                    display:flex;
                    justify-content:flex-end;
                    gap:10px;
                "
            >

                <a
                    class="btn btn-outline"
                    href="<%=request.getContextPath()%>/manager/dashboard"
                >

                    Cancel

                </a>


                <button
                    type="submit"
                    class="btn btn-primary"
                >

                    Create Project

                </button>

            </div>


        </form>

    </div>

</div>


<!-- DATE VALIDATION -->

<script>

    const startDate =
        document.getElementById("startDate");

    const endDate =
        document.getElementById("endDate");


    function updateEndDateMinimum() {

        if (startDate.value) {

            endDate.min =
                startDate.value;

        }

    }


    startDate.addEventListener(
        "change",
        updateEndDateMinimum
    );


    updateEndDateMinimum();


    document.querySelector("form").addEventListener(
        "submit",
        function(event) {

            if (
                startDate.value &&
                endDate.value &&
                endDate.value < startDate.value
            ) {

                event.preventDefault();

                endDate.setCustomValidity(
                    "End date cannot be before the start date."
                );

                endDate.reportValidity();

            } else {

                endDate.setCustomValidity("");

            }

        }
    );

</script>


</body>

</html>
