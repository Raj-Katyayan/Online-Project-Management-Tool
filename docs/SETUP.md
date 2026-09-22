# Setup / Recovery Guide

## 1. MySQL

Open MySQL Workbench and run `database/project_management.sql`.

Your existing database can be reused; the statements use `IF NOT EXISTS`.

## 2. Database password

Edit:

`src/main/resources/db.properties`

Set:

`db.username=root`
`db.password=YOUR_MYSQL_PASSWORD`

The password stays local.

## 3. Build

Open CMD in the project root (the folder that contains `pom.xml`):

```cmd
mvn clean package
```

## 4. Deploy to your existing Tomcat

```cmd
copy /Y target\project-management-tool.war "C:\Coding & Skills\apache-tomcat-10.1.60\webapps\project-management-tool.war"
```

Restart Tomcat and open:

`http://localhost:8080/project-management-tool/`

## 5. First real accounts

Registration always creates a MEMBER account. Use MySQL Workbench or the Admin dashboard to change the intended manager to MANAGER.

For your current project, keep your real account as MANAGER and your three teammates as MEMBER accounts.

## 6. Clean URLs

Use these routes:

`/manager/dashboard`
`/member/dashboard`
`/admin/dashboard`

Do not use `/manager/dashboard.jsp` as a normal browser URL.
