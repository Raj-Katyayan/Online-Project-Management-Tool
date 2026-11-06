package com.jvmcrew.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ManagerDashboardServiceValidationTest {

    private final ManagerDashboardService dashboardService =
            new ManagerDashboardService();

    @Test
    void shouldRejectInvalidManagerIdWhenLoadingProjects() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getProjects(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenCountingProjects() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getTotalProjects(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenCountingTasks() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getTotalTasks(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenCountingInProgressTasks() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getInProgressTasks(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenCountingCompletedTasks() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getCompletedTasks(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenCountingTeamMembers() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getTeamMembers(0)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenSearchingProjects() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.searchProjects(
                        0,
                        "project",
                        "IN_PROGRESS",
                        "HIGH"
                )
        );
    }

    @Test
    void shouldRejectInvalidManagerIdWhenLoadingProjectProgress() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService.getProjectProgress(0)
        );
    }
}
