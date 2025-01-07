package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.AppointmentDTO;
import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;


    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentDTO dummyAppointmentDTO;
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

        dummyAppointmentDTO = new AppointmentDTO();
        dummyAppointmentDTO.setId(1L);
        dummyAppointmentDTO.setPatientId(1L);
        dummyAppointmentDTO.setAppointmentDate(LocalDate.of(2024, 12, 1));
        dummyAppointmentDTO.setReason("Routine check-up");

        dummyAppointment = new Appointment();
        dummyAppointment.setId(1L);
        dummyAppointment.setPatient(dummyPatient);
        dummyAppointment.setAppointmentDate(LocalDate.of(2024, 12, 1));
        dummyAppointment.setReason("Routine check-up");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(dummyPatient));

    }

    @Test
    void saveAppointment_Success() {
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
            Appointment savedAppointment = invocation.getArgument(0);
            savedAppointment.setId(1L); // Simulează generarea ID-ului
            return savedAppointment;
        });

        Appointment result = appointmentService.saveAppointment(dummyAppointmentDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Routine check-up", result.getReason());
        assertEquals(1L, result.getPatient().getId());
        verify(patientRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void saveAppointment_PatientNotFound_ThrowsException() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(2L);
        appointmentDTO.setAppointmentDate(LocalDate.of(2024, 12, 1));
        appointmentDTO.setReason("Check-up");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.saveAppointment(appointmentDTO);
        });

        assertEquals("Patient not found with ID: 2", exception.getMessage());
        verify(patientRepository, times(1)).findById(2L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
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

    @Test
    void getAllAppointments_Success() {
        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setPatient(dummyPatient);
        appointment1.setAppointmentDate(LocalDate.of(2024, 12, 1));
        appointment1.setReason("Routine check-up");

        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        appointment2.setPatient(dummyPatient);
        appointment2.setAppointmentDate(LocalDate.of(2024, 12, 2));
        appointment2.setReason("Dental cleaning");

        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(appointment1, appointment2));

        List<Appointment> result = appointmentService.getAllAppointments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Routine check-up", result.get(0).getReason());
        assertEquals("Dental cleaning", result.get(1).getReason());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getTodayAppointments_Success() {
        LocalDate today = LocalDate.now();
        Appointment todayAppointment = new Appointment();
        todayAppointment.setId(1L);
        todayAppointment.setPatient(dummyPatient);
        todayAppointment.setAppointmentDate(today);
        todayAppointment.setReason("Routine check-up");

        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(todayAppointment));

        List<AppointmentDTO> result = appointmentService.getTodayAppointments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(today, result.get(0).getAppointmentDate());
        assertEquals("Routine check-up", result.get(0).getReason());
        verify(appointmentRepository, times(1)).findAll();
    }


}
