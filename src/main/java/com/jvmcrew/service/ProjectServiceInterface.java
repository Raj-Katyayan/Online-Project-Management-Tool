package com.jvmcrew.service;

import com.jvmcrew.model.Project;

public interface ProjectServiceInterface {

    boolean createProject(Project project) throws Exception;
}