package com.jvmcrew.service;

import com.jvmcrew.model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateTaskServiceValidationTest {

    private final CreateTaskService taskService = new CreateTaskService();

    @Test
    void shouldRejectNullTask() {
        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(null)
        );
    }

    @Test
    void shouldRejectNonPositiveProjectId() {
        Task task = validTask();
        task.setProjectId(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectNullTitle() {
        Task task = validTask();
        task.setTitle(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectBlankTitle() {
        Task task = validTask();
        task.setTitle("   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectTitleShorterThanTwoCharacters() {
        Task task = validTask();
        task.setTitle("A");

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectTitleLongerThan150Characters() {
        Task task = validTask();
        task.setTitle("T".repeat(151));

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectDescriptionLongerThan1000Characters() {
        Task task = validTask();
        task.setDescription("D".repeat(1001));

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectMissingStartDate() {
        Task task = validTask();
        task.setStartDate(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectMissingDueDate() {
        Task task = validTask();
        task.setDueDate(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectDueDateBeforeStartDate() {
        Task task = validTask();
        task.setStartDate(LocalDate.of(2026, 11, 10));
        task.setDueDate(LocalDate.of(2026, 11, 9));

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectInvalidPriority() {
        Task task = validTask();
        task.setPriority("URGENT");

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.createTask(task)
        );
    }

    @Test
    void shouldRejectInvalidManagerIdBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getManagerProject(1, 0)
        );
    }

    @Test
    void shouldRejectInvalidProjectIdBeforeDatabaseAccess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.getManagerProject(0, 1)
        );
    }

    private Task validTask() {
        return new Task(
                1,
                "Prepare report",
                "Validation test task",
                "MEDIUM",
                "TODO",
                LocalDate.of(2026, 11, 5),
                LocalDate.of(2026, 11, 15)
        );
    }
}
