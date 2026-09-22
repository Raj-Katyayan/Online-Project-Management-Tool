package com.jvmcrew.model;

import java.time.LocalDate;

public class Project {
    private int projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String priority;
    private String status;
    private int managerId;

    public Project() {
    }

    public Project(String projectName, String description, LocalDate startDate,
                   LocalDate endDate, String priority, String status, int managerId) {
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.status = status;
        this.managerId = managerId;
    }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
}
