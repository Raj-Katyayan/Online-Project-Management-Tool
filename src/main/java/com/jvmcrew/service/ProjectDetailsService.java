package com.jvmcrew.service;

import com.jvmcrew.dao.ProjectDAO;
import com.jvmcrew.dao.ProjectMemberDAO;
import com.jvmcrew.dao.TaskAssignmentDAO;
import com.jvmcrew.dao.TaskDAO;
import com.jvmcrew.exception.ProjectNotFoundException;
import com.jvmcrew.model.Project;
import com.jvmcrew.model.Task;
import com.jvmcrew.model.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectDetailsService implements ProjectDetailsServiceInterface {

    private final ProjectDAO projectDAO =
            new ProjectDAO();

    private final ProjectMemberDAO memberDAO =
            new ProjectMemberDAO();

    private final TaskDAO taskDAO =
            new TaskDAO();

    private final TaskAssignmentDAO assignmentDAO =
            new TaskAssignmentDAO();


    /*
     * Loads all information required by the
     * Manager Project Details page.
     */
    @Override
    public ProjectDetailsData getProjectDetails(
            int projectId,
            int managerId)
            throws SQLException, ProjectNotFoundException {

        validateIds(projectId, managerId);


        /*
         * Verify that the project belongs
         * to the logged-in manager.
         */
        Project project =
                projectDAO.getProjectForManager(
                        projectId,
                        managerId
                );


        if (project == null) {
            throw new ProjectNotFoundException(
                    "Project not found or you do not have access to this project."
            );
        }


        /*
         * Load project members.
         */
        List<User> members =
                memberDAO.getMembersByProject(projectId);


        /*
         * Load project tasks.
         */
        List<Task> tasks =
                taskDAO.getTasksByProject(projectId);


        /*
         * Load assignees for every task.
         */
        Map<Integer, List<Integer>> assigneesByTask =
                new HashMap<>();


        for (Task task : tasks) {

            assigneesByTask.put(
                    task.getTaskId(),
                    assignmentDAO.getAssigneeIds(
                            task.getTaskId()
                    )
            );
        }


        return new ProjectDetailsData(
                project,
                members,
                tasks,
                assigneesByTask
        );
    }


    /*
     * Basic service-level validation.
     */
    private void validateIds(
            int projectId,
            int managerId) {

        if (projectId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid project ID."
            );
        }

        if (managerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid manager information."
            );
        }
    }


    /*
     * DTO-style container used to transfer
     * all project details from Service to Servlet.
     */
    public static class ProjectDetailsData {

        private final Project project;
        private final List<User> members;
        private final List<Task> tasks;
        private final Map<Integer, List<Integer>>
                assigneesByTask;


        public ProjectDetailsData(
                Project project,
                List<User> members,
                List<Task> tasks,
                Map<Integer, List<Integer>> assigneesByTask) {

            this.project = project;
            this.members = members;
            this.tasks = tasks;
            this.assigneesByTask = assigneesByTask;
        }


        public Project getProject() {
            return project;
        }


        public List<User> getMembers() {
            return members;
        }


        public List<Task> getTasks() {
            return tasks;
        }


        public Map<Integer, List<Integer>>
        getAssigneesByTask() {

            return assigneesByTask;
        }
    }
}