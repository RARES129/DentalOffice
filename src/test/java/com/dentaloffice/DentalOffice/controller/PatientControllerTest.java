package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.PatientDTO;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.PatientMapper;
import com.dentaloffice.DentalOffice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstName("John");
        Patient patient = PatientMapper.toEntity(patientDTO);

        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        ResponseEntity<PatientDTO> response = patientController.createPatient(patientDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(patientService, times(1)).savePatient(any(Patient.class));
    }

    @Test
    void getPatientById() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");

        when(patientService.getPatientById(1L)).thenReturn(Optional.of(patient));

        ResponseEntity<PatientDTO> response = patientController.getPatientById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    void getAllPatients() {
        Patient patient1 = new Patient();
        patient1.setFirstName("John");
        patient1.setLastName("Doe");
        Patient patient2 = new Patient();
        patient2.setFirstName("Jane");
        patient2.setLastName("Doe");

        when(patientService.getAllPatientsOrderedByLastName()).thenReturn(Arrays.asList(patient1, patient2));

        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatientsSorted();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientService, times(1)).getAllPatientsOrderedByLastName();
    }

    @Test
    void deletePatient() {
        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void updatePatient_Success() {
        // Create a dummy patient DTO to be updated
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstName("Rares");
        patientDTO.setLastName("Dascalu");

        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setFirstName("John");
        existingPatient.setLastName("Doe");

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstName("Rares");
        updatedPatient.setLastName("Dascalu");

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        ResponseEntity<PatientDTO> response = patientController.updatePatient(1L, patientDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rares", response.getBody().getFirstName());
        assertEquals("Dascalu", response.getBody().getLastName());
        verify(patientService, times(1)).updatePatient(eq(1L), any(Patient.class));
    }

    @Test
    void getPatientByEmail_Success() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("johndoe@example.com");

        when(patientService.getPatientByEmail("johndoe@example.com")).thenReturn(Optional.of(patient));

        ResponseEntity<PatientDTO> response = patientController.getPatientByEmail("johndoe@example.com");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
        assertEquals("johndoe@example.com", response.getBody().getEmail());

        verify(patientService, times(1)).getPatientByEmail("johndoe@example.com");
    }

    @Test
    void getPatientByEmail_NotFound() {

        when(patientService.getPatientByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        ResponseEntity<PatientDTO> response = patientController.getPatientByEmail("nonexistent@example.com");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(patientService, times(1)).getPatientByEmail("nonexistent@example.com");
    }

}
