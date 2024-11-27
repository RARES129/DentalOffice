package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePatient() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.savePatient(patient);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void getPatientById() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getAllPatients() {
        Patient patient1 = new Patient();
        patient1.setFirstName("John");
        Patient patient2 = new Patient();
        patient2.setFirstName("Jane");

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void deletePatient() {
        patientService.deletePatient(1L);
        verify(patientRepository, times(1)).deleteById(1L);
    }
}
