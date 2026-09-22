@echo off
setlocal
set "TOMCAT_HOME=C:\Coding & Skills\apache-tomcat-10.1.60"
cd /d "%~dp0"
call mvn clean package
if errorlevel 1 (
  echo BUILD FAILED.
  exit /b 1
)
copy /Y "target\project-management-tool.war" "%TOMCAT_HOME%\webapps\project-management-tool.war"
if errorlevel 1 (
  echo COPY FAILED. Check TOMCAT_HOME in this file.
  exit /b 1
)
echo WAR deployed. Restart Tomcat before testing.
pause
