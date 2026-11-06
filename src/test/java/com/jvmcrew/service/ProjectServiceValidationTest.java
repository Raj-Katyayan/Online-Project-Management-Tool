package com.jvmcrew.service;

import com.jvmcrew.exception.ValidationException;
import com.jvmcrew.model.Project;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectServiceValidationTest {

    private final ProjectService projectService = new ProjectService();

    @Test
    void shouldRejectNullProject() {
        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(null)
        );
    }

    @Test
    void shouldRejectBlankProjectName() {
        Project project = validProject();
        project.setProjectName("   ");

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectProjectNameShorterThanTwoCharacters() {
        Project project = validProject();
        project.setProjectName("A");

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectProjectNameLongerThan150Characters() {
        Project project = validProject();
        project.setProjectName("P".repeat(151));

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectDescriptionLongerThan1000Characters() {
        Project project = validProject();
        project.setDescription("D".repeat(1001));

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectMissingStartDate() {
        Project project = validProject();
        project.setStartDate(null);

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectMissingEndDate() {
        Project project = validProject();
        project.setEndDate(null);

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        Project project = validProject();
        project.setStartDate(LocalDate.of(2026, 11, 10));
        project.setEndDate(LocalDate.of(2026, 11, 9));

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectInvalidPriority() {
        Project project = validProject();
        project.setPriority("URGENT");

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectInvalidStatus() {
        Project project = validProject();
        project.setStatus("ARCHIVED");

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    @Test
    void shouldRejectNonPositiveManagerId() {
        Project project = validProject();
        project.setManagerId(0);

        assertThrows(
                ValidationException.class,
                () -> projectService.createProject(project)
        );
    }

    private Project validProject() {
        return new Project(
                "Review Project",
                "Validation test project",
                LocalDate.of(2026, 11, 5),
                LocalDate.of(2026, 11, 20),
                "MEDIUM",
                "PLANNED",
                1
        );
    }
}
