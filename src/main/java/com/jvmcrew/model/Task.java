package com.jvmcrew.model;

import java.time.LocalDate;

public class Task {
    private int taskId;
    private int projectId;
    private String projectName;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDate startDate;
    private LocalDate dueDate;

    public Task() {
    }

    public Task(int projectId, String title, String description, String priority,
                String status, LocalDate startDate, LocalDate dueDate) {
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
