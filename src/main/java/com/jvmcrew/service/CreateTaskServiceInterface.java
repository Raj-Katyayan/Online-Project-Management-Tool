package com.jvmcrew.service;

import com.jvmcrew.model.Project;
import com.jvmcrew.model.Task;

public interface CreateTaskServiceInterface {

    Project getManagerProject(int projectId, int managerId) throws Exception;

    boolean createTask(Task task) throws Exception;
}