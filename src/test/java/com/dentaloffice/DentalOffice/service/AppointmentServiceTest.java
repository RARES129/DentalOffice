package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment dummyAppointment;
    private Patient dummyPatient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create dummy patient
        dummyPatient = new Patient();
        dummyPatient.setId(1L);
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");

        // Create dummy appointment
        dummyAppointment = new Appointment();
        dummyAppointment.setId(1L);
        dummyAppointment.setPatient(dummyPatient);
        dummyAppointment.setAppointmentDate(new Date());
        dummyAppointment.setReason("Routine check-up");
    }

    @Test
    void saveAppointment_Success() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(dummyAppointment);

        Appointment result = appointmentService.saveAppointment(dummyAppointment);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Routine check-up", result.getReason());
        assertEquals(1L, result.getPatient().getId());
        verify(appointmentRepository, times(1)).save(dummyAppointment);
    }

    @Test
    void saveAppointment_NullAppointment_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.saveAppointment(null);
        });
        assertEquals("Appointment cannot be null", exception.getMessage());
    }

    @Test
    void getAppointmentById_Success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(dummyAppointment));

        Optional<Appointment> result = appointmentService.getAppointmentById(1L);

        assertTrue(result.isPresent());
        assertEquals("Routine check-up", result.get().getReason());
        assertEquals(1L, result.get().getPatient().getId());
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAppointmentById_NullId_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointmentById(null);
        });
        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }

    @Test
    void getAppointmentsByPatientId_Success() {
        when(appointmentRepository.findByPatientId(1L)).thenReturn(Arrays.asList(dummyAppointment));

        List<Appointment> result = appointmentService.getAppointmentsByPatientId(1L);

        assertEquals(1, result.size());
        assertEquals("Routine check-up", result.get(0).getReason());
        assertEquals(1L, result.get(0).getPatient().getId());
        verify(appointmentRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void getAppointmentsByPatientId_NullPatientId_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointmentsByPatientId(null);
        });
        assertEquals("Patient ID cannot be null", exception.getMessage());
    }

    @Test
    void deleteAppointment_Success() {
        doNothing().when(appointmentRepository).deleteById(1L);

        appointmentService.deleteAppointment(1L);

        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAppointment_NullId_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.deleteAppointment(null);
        });
        assertEquals("Appointment ID cannot be null", exception.getMessage());
    }
}
