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

    @Test
    void getPatientByEmail_Success() {
        // Prepare a test patient with email
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("johndoe@example.com");

        // Mock the service call
        when(patientRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(patient));

        // Call the service method
        Optional<Patient> result = patientService.getPatientByEmail("johndoe@example.com");

        // Validate the result
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        assertEquals("johndoe@example.com", result.get().getEmail());

        // Verify the repository call
        verify(patientRepository, times(1)).findByEmail("johndoe@example.com");
    }

    @Test
    void getPatientByEmail_NotFound() {
        // Mock the repository call to return empty for a non-existing email
        when(patientRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Call the service method
        Optional<Patient> result = patientService.getPatientByEmail("nonexistent@example.com");

        // Validate the result
        assertFalse(result.isPresent());

        // Verify the repository call
        verify(patientRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void getAllPatientsOrderedByLastName() {
        Patient patient1 = new Patient();
        patient1.setFirstName("John");
        patient1.setLastName("Doe");

        Patient patient2 = new Patient();
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");

        // Mock the repository method to return sorted patients
        when(patientRepository.findAllOrderedByLastName()).thenReturn(Arrays.asList(patient2, patient1));

        // Call the service method
        List<Patient> result = patientService.getAllPatientsOrderedByLastName();

        // Validate the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Smith", result.get(0).getLastName());
        assertEquals("Doe", result.get(1).getLastName());

        // Verify the repository call
        verify(patientRepository, times(1)).findAllOrderedByLastName();
    }

    @Test
    void updatePatient_Success() {
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setFirstName("John");
        existingPatient.setLastName("Doe");

        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Rares");
        updatedPatient.setLastName("Dascalu");

        // Mock the repository behavior for finding and saving the updated patient
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(updatedPatient);

        // Call the service method
        Patient result = patientService.updatePatient(1L, updatedPatient);

        // Validate the result
        assertNotNull(result);
        assertEquals("Rares", result.getFirstName());
        assertEquals("Dascalu", result.getLastName());

        // Verify the repository calls
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    void updatePatient_PatientNotFound() {
        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstName("Rares");
        updatedPatient.setLastName("Dascalu");

        // Mock the repository behavior for patient not found
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method
        Patient result = patientService.updatePatient(1L, updatedPatient);

        // Validate the result
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rares", result.getFirstName());
        assertEquals("Dascalu", result.getLastName());

        // Verify the repository calls
        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(updatedPatient);
    }


}
