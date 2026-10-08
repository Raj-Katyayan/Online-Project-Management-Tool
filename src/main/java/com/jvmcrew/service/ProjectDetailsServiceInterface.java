package com.jvmcrew.service;

import com.jvmcrew.model.Project;
import com.jvmcrew.model.Task;
import com.jvmcrew.model.User;

import java.util.List;
import java.util.Map;

public interface ProjectDetailsServiceInterface {

    ProjectDetailsService.ProjectDetailsData getProjectDetails(
            int projectId,
            int managerId
    ) throws Exception;
}