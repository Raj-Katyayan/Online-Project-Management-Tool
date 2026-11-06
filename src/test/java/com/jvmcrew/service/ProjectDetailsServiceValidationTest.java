package com.jvmcrew.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectDetailsServiceValidationTest {

    private final ProjectDetailsService detailsService =
            new ProjectDetailsService();

    @Test
    void shouldRejectInvalidProjectIdBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> detailsService.getProjectDetails(0, 1)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> detailsService.getProjectDetails(1, 0)
        );
    }
}
