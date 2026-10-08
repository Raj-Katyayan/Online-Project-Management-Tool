package com.jvmcrew.service;

public interface ProjectStatusServiceInterface {

    boolean updateStatus(
            int projectId,
            int managerId,
            String status
    ) throws Exception;
}