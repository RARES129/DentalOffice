package com.dentaloffice.DentalOffice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dentaloffice.DentalOffice.service.ReminderService;

@ExtendWith(MockitoExtension.class)
public class ReminderControllerTest {

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private ReminderController reminderController;


    @Test
    void sendAppointmentReminder_Success() {
        Long appointmentId = 1L;

        doNothing().when(reminderService).sendAppointmentReminder(appointmentId);

        ResponseEntity<Void> response = reminderController.sendAppointmentReminder(appointmentId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(reminderService, times(1)).sendAppointmentReminder(appointmentId);
    }

    @Test
    void sendAppointmentReminder_AppointmentNotFound() {
        Long appointmentId = 1L;

        doThrow(new IllegalArgumentException("Appointment not found")).when(reminderService).sendAppointmentReminder(appointmentId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderController.sendAppointmentReminder(appointmentId);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(reminderService, times(1)).sendAppointmentReminder(appointmentId);
    }
}
