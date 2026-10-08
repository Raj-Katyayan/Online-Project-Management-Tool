package com.jvmcrew.service;

import com.jvmcrew.model.Project;

import java.util.List;
import java.util.Map;

public interface ManagerDashboardServiceInterface {

    List<Project> getProjects(
            int managerId
    ) throws Exception;


    int getTotalProjects(
            int managerId
    ) throws Exception;


    int getTotalTasks(
            int managerId
    ) throws Exception;


    int getInProgressTasks(
            int managerId
    ) throws Exception;


    int getCompletedTasks(
            int managerId
    ) throws Exception;


    int getTeamMembers(
            int managerId
    ) throws Exception;


    List<Project> searchProjects(
            int managerId,
            String search,
            String status,
            String priority
    ) throws Exception;


    Map<Integer, Integer> getProjectProgress(
            int managerId
    ) throws Exception;
}