package com.jvmcrew.service;

import com.jvmcrew.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectStatusServiceValidationTest {

    private final ProjectStatusService statusService =
            new ProjectStatusService();


    @Test
    void shouldRejectInvalidProjectId() {

        assertThrows(
                ValidationException.class,
                () -> statusService.updateStatus(
                        0,
                        2,
                        "PLANNED"
                )
        );
    }


    @Test
    void shouldRejectInvalidManagerId() {

        assertThrows(
                ValidationException.class,
                () -> statusService.updateStatus(
                        1,
                        0,
                        "PLANNED"
                )
        );
    }


    @Test
    void shouldRejectBlankStatus() {

        assertThrows(
                ValidationException.class,
                () -> statusService.updateStatus(
                        1,
                        2,
                        "   "
                )
        );
    }


    @Test
    void shouldRejectInvalidStatus() {

        assertThrows(
                ValidationException.class,
                () -> statusService.updateStatus(
                        1,
                        2,
                        "INVALID_STATUS"
                )
        );
    }
}