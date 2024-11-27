package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
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

class MedicalNoteServiceTest {

    @Mock
    private MedicalNoteRepository medicalNoteRepository;

    @InjectMocks
    private MedicalNoteService medicalNoteService;

    private MedicalNote dummyNote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyNote = new MedicalNote();
        dummyNote.setId(1L);
        dummyNote.setNote("This is a medical note.");
    }

    @Test
    void saveMedicalNote_Success() {
        when(medicalNoteRepository.save(any(MedicalNote.class))).thenReturn(dummyNote);

        MedicalNote result = medicalNoteService.saveMedicalNote(dummyNote);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalNoteRepository, times(1)).save(dummyNote);
    }

    @Test
    void saveMedicalNote_NullMedicalNote_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> medicalNoteService.saveMedicalNote(null),
                "MedicalNote cannot be null");
    }

    @Test
    void getMedicalNoteById_Success() {
        when(medicalNoteRepository.findById(1L)).thenReturn(Optional.of(dummyNote));

        Optional<MedicalNote> result = medicalNoteService.getMedicalNoteById(1L);

        assertTrue(result.isPresent());
        assertEquals("This is a medical note.", result.get().getNote());
        verify(medicalNoteRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalNoteById_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> medicalNoteService.getMedicalNoteById(null),
                "MedicalNote ID cannot be null");
    }

    @Test
    void getMedicalNotesByPatientId_Success() {
        Long patientId = 1L;
        MedicalNote medicalNote = new MedicalNote(); // Assuming constructor and setters
        when(medicalNoteRepository.findByPatientId(patientId)).thenReturn(Arrays.asList(medicalNote));

        List<MedicalNote> result = medicalNoteService.getMedicalNotesByPatientId(patientId);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(medicalNoteRepository).findByPatientId(patientId);
    }

    @Test
    void getMedicalNotesByPatientId_NullPatientId_ThrowsIllegalArgumentException() {
        Long patientId = null;
        assertThrows(IllegalArgumentException.class, () -> {
            medicalNoteService.getMedicalNotesByPatientId(patientId);
        });
    }


    @Test
    void getAllMedicalNotes_Success() {
        when(medicalNoteRepository.findAll()).thenReturn(Arrays.asList(dummyNote));

        List<MedicalNote> result = medicalNoteService.getAllMedicalNotes();

        assertEquals(1, result.size());
        verify(medicalNoteRepository, times(1)).findAll();
    }

    @Test
    void deleteMedicalNote_Success() {
        doNothing().when(medicalNoteRepository).deleteById(1L);

        medicalNoteService.deleteMedicalNote(1L);

        verify(medicalNoteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalNote_NullId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> medicalNoteService.deleteMedicalNote(null),
                "MedicalNote ID cannot be null");
    }
}
