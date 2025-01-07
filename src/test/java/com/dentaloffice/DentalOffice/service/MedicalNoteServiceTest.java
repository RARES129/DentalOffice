package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
import com.dentaloffice.DentalOffice.dto.PatientMedicalNotesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalNoteServiceTest {

    @Mock
    private MedicalNoteRepository medicalNoteRepository;

    @InjectMocks
    private MedicalNoteService medicalNoteService;

    private Patient patient;
    private MedicalNote medicalNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");

        medicalNote = new MedicalNote();
        medicalNote.setId(1L);
        medicalNote.setNote("Test Note");
        medicalNote.setPatient(patient);
    }

    @Test
    void saveMedicalNote_Success() {
        when(medicalNoteRepository.save(medicalNote)).thenReturn(medicalNote);

        MedicalNote result = medicalNoteService.saveMedicalNote(medicalNote);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalNoteRepository, times(1)).save(medicalNote);
    }

    @Test
    void saveMedicalNote_NullMedicalNote_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> medicalNoteService.saveMedicalNote(null),
                "Expected IllegalArgumentException for null MedicalNote"
        );
    }

    @Test
    void getMedicalNoteById_Success() {
        when(medicalNoteRepository.findById(1L)).thenReturn(Optional.of(medicalNote));

        Optional<MedicalNote> result = medicalNoteService.getMedicalNoteById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(medicalNoteRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalNoteById_NotFound() {
        when(medicalNoteRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<MedicalNote> result = medicalNoteService.getMedicalNoteById(2L);

        assertFalse(result.isPresent());
        verify(medicalNoteRepository, times(1)).findById(2L);
    }

    @Test
    void getMedicalNotesByPatientId_Success() {
        when(medicalNoteRepository.findByPatientId(1L)).thenReturn(Arrays.asList(medicalNote));

        List<PatientMedicalNotesDTO> result = medicalNoteService.getMedicalNotesByPatientId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(medicalNoteRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void getMedicalNotesByPatientId_EmptyList() {
        when(medicalNoteRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        List<PatientMedicalNotesDTO> result = medicalNoteService.getMedicalNotesByPatientId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(medicalNoteRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void getAllMedicalNotesOrderedByPatientLastName_Success() {
        when(medicalNoteRepository.findAllOrderedByPatientLastName()).thenReturn(Arrays.asList(medicalNote));

        List<PatientMedicalNotesDTO> result = medicalNoteService.getAllMedicalNotesOrderedByPatientLastName();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        verify(medicalNoteRepository, times(1)).findAllOrderedByPatientLastName();
    }

    @Test
    void deleteMedicalNote_Success() {
        doNothing().when(medicalNoteRepository).deleteById(1L);

        medicalNoteService.deleteMedicalNote(1L);

        verify(medicalNoteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalNote_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> medicalNoteService.deleteMedicalNote(null),
                "Expected IllegalArgumentException for null ID"
        );
    }
}
