package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.AppointmentDTO;
import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.AppointmentMapper;
import com.dentaloffice.DentalOffice.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;
    private Patient dummyPatient;
    private Appointment dummyAppointment;
    private AppointmentDTO dummyAppointmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyPatient = new Patient();
        dummyPatient.setId(1L);
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");

        // Create dummy appointment
        dummyAppointment = new Appointment();
        dummyAppointment.setId(1L);
        dummyAppointment.setPatient(dummyPatient);
        dummyAppointment.setAppointmentDate(LocalDate.of(2002, 9, 12));
        dummyAppointment.setReason("Routine check-up");

        // Create dummy DTO
        dummyAppointmentDTO = AppointmentMapper.toDTO(dummyAppointment);
    }

    @Test
    void createAppointment_Success() {
        when(appointmentService.saveAppointment(any(AppointmentDTO.class))).thenReturn(dummyAppointment);

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(dummyAppointmentDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Routine check-up", response.getBody().getReason());
        assertEquals(1L, response.getBody().getPatientId());
        verify(appointmentService, times(1)).saveAppointment(any(AppointmentDTO.class));
    }

    @Test
    void createAppointment_IllegalArgumentException_ReturnsBadRequest() {
        // Simulate IllegalArgumentException thrown from the service
        when(appointmentService.saveAppointment(any(AppointmentDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid appointment"));

        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(dummyAppointmentDTO);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());  // Check for bad request status
        verify(appointmentService, times(1)).saveAppointment(any(AppointmentDTO.class));
    }


    @Test
    void createAppointment_NullBody_ReturnsBadRequest() {
        ResponseEntity<AppointmentDTO> response = appointmentController.createAppointment(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(appointmentService, never()).saveAppointment(any());
    }

    @Test
    void getAppointmentById_Success() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(Optional.of(dummyAppointment));

        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Routine check-up", response.getBody().getReason());
        assertEquals(1L, response.getBody().getPatientId());
        verify(appointmentService, times(1)).getAppointmentById(1L);
    }

    @Test
    void getAppointmentById_NullId_ReturnsBadRequest() {
        ResponseEntity<AppointmentDTO> response = appointmentController.getAppointmentById(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(appointmentService, never()).getAppointmentById(any());
    }

    @Test
    void getAppointmentsByPatientId_Success() {
        when(appointmentService.getAppointmentsByPatientId(1L)).thenReturn(Arrays.asList(dummyAppointment));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByPatientId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Routine check-up", response.getBody().get(0).getReason());
        assertEquals(1L, response.getBody().get(0).getPatientId());
        verify(appointmentService, times(1)).getAppointmentsByPatientId(1L);
    }

    @Test
    void getAppointmentsByPatientId_NullPatientId_ReturnsBadRequest() {
        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAppointmentsByPatientId(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(appointmentService, never()).getAppointmentsByPatientId(any());
    }

    @Test
    void deleteAppointment_Success() {
        doNothing().when(appointmentService).deleteAppointment(1L);

        ResponseEntity<Void> response = appointmentController.deleteAppointment(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(appointmentService, times(1)).deleteAppointment(1L);
    }

    @Test
    void deleteAppointment_NullId_ReturnsBadRequest() {
        ResponseEntity<Void> response = appointmentController.deleteAppointment(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(appointmentService, never()).deleteAppointment(any());
    }

    @Test
    void getAllAppointments_Success() {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList(dummyAppointment));

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAllAppointments();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Routine check-up", response.getBody().get(0).getReason());
        assertEquals(1L, response.getBody().get(0).getPatientId());
        verify(appointmentService, times(1)).getAllAppointments();
    }

    @Test
    void getAllAppointments_NoAppointments_ReturnsNotFound() {
        when(appointmentService.getAllAppointments()).thenReturn(Arrays.asList());

        ResponseEntity<List<AppointmentDTO>> response = appointmentController.getAllAppointments();

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(appointmentService, times(1)).getAllAppointments();
    }


    @Test
    void getTodayAppointments_Success() {
        List<AppointmentDTO> appointmentDTOList = Arrays.asList(dummyAppointmentDTO);
        when(appointmentService.getTodayAppointments()).thenReturn(appointmentDTOList);

        ResponseEntity<?> response = appointmentController.getTodayAppointments();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, ((List<AppointmentDTO>) response.getBody()).size());
        assertEquals("Routine check-up", ((List<AppointmentDTO>) response.getBody()).get(0).getReason());
        verify(appointmentService, times(1)).getTodayAppointments();
    }

    @Test
    void getTodayAppointments_NoAppointments_ReturnsNotFound() {
        when(appointmentService.getTodayAppointments()).thenReturn(Arrays.asList());

        ResponseEntity<?> response = appointmentController.getTodayAppointments();

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No appointments today.", response.getBody());
        verify(appointmentService, times(1)).getTodayAppointments();
    }


}
