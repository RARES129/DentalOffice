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
        Patient patient2 = new Patient();
        patient2.setFirstName("Jane");

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient1, patient2));

        ResponseEntity<List<PatientDTO>> response = patientController.getAllPatients();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    void deletePatient() {
        ResponseEntity<Void> response = patientController.deletePatient(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(patientService, times(1)).deletePatient(1L);
    }
}
