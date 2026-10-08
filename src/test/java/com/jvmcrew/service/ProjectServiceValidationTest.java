package com.jvmcrew.service;

import com.jvmcrew.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectServiceValidationTest {

    private final ProjectService projectService =
            new ProjectService();


    @Test
    void shouldRejectNullProject() {

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(null)
        );
    }
}