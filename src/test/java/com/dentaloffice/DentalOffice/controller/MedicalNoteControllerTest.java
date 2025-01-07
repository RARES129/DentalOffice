package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.MedicalNoteDTO;
import com.dentaloffice.DentalOffice.dto.PatientMedicalNotesDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.MedicalNoteMapper;
import com.dentaloffice.DentalOffice.service.MedicalNoteService;
import com.dentaloffice.DentalOffice.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalNoteControllerTest {

    @Mock
    private MedicalNoteService medicalNoteService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private MedicalNoteController medicalNoteController;

    private Patient dummyPatient;
    private MedicalNote dummyNote;
    private MedicalNoteDTO dummyNoteDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyPatient = new Patient();
        dummyPatient.setId(1L);
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");

        dummyNote = new MedicalNote();
        dummyNote.setId(1L);
        dummyNote.setPatient(dummyPatient);
        dummyNote.setNote("This is a medical note.");

        dummyNoteDTO = MedicalNoteMapper.toDTO(dummyNote);

        when(patientService.getPatientById(anyLong())).thenReturn(Optional.of(dummyPatient));
        when(medicalNoteService.saveMedicalNote(any(MedicalNote.class))).thenReturn(dummyNote);
    }

    @Test
    void addMedicalNote_Success() {
        ResponseEntity<MedicalNoteDTO> response = medicalNoteController.addMedicalNote(dummyNoteDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dummyNoteDTO.getNote(), response.getBody().getNote());
        verify(medicalNoteService, times(1)).saveMedicalNote(any(MedicalNote.class));
    }

    @Test
    void addMedicalNote_NullBody_ReturnsBadRequest() {
        ResponseEntity<MedicalNoteDTO> response = medicalNoteController.addMedicalNote(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(medicalNoteService, never()).saveMedicalNote(any());
    }

    @Test
    void getMedicalNoteById_Success() {
        when(medicalNoteService.getMedicalNoteById(1L)).thenReturn(Optional.of(dummyNote));

        ResponseEntity<MedicalNoteDTO> response = medicalNoteController.getMedicalNoteById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dummyNote.getNote(), response.getBody().getNote());
        verify(medicalNoteService, times(1)).getMedicalNoteById(1L);
    }

    @Test
    void getMedicalNoteById_NotFound() {
        when(medicalNoteService.getMedicalNoteById(1L)).thenReturn(Optional.empty());

        ResponseEntity<MedicalNoteDTO> response = medicalNoteController.getMedicalNoteById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(medicalNoteService, times(1)).getMedicalNoteById(1L);
    }

    @Test
    void getMedicalNoteById_NullId_ReturnsBadRequest() {
        ResponseEntity<MedicalNoteDTO> response = medicalNoteController.getMedicalNoteById(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(medicalNoteService, never()).getMedicalNoteById(any());
    }

    @Test
    void getAllMedicalNotes_Success() {
        when(medicalNoteService.getAllMedicalNotesOrderedByPatientLastName())
                .thenReturn(Collections.singletonList(new PatientMedicalNotesDTO()));

        ResponseEntity<List<PatientMedicalNotesDTO>> response = medicalNoteController.getAllMedicalNotes();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(medicalNoteService, times(1)).getAllMedicalNotesOrderedByPatientLastName();
    }

    @Test
    void getAllMedicalNotes_NoNotes() {
        when(medicalNoteService.getAllMedicalNotesOrderedByPatientLastName()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PatientMedicalNotesDTO>> response = medicalNoteController.getAllMedicalNotes();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(medicalNoteService, times(1)).getAllMedicalNotesOrderedByPatientLastName();
    }

    @Test
    void getMedicalNotesByPatientId_Success() {
        when(medicalNoteService.getMedicalNotesByPatientId(1L)).thenReturn(Collections.singletonList(new PatientMedicalNotesDTO()));

        ResponseEntity<List<PatientMedicalNotesDTO>> response = medicalNoteController.getMedicalNotesByPatientId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(medicalNoteService, times(1)).getMedicalNotesByPatientId(1L);
    }

    @Test
    void getMedicalNotesByPatientId_NotFound() {
        when(medicalNoteService.getMedicalNotesByPatientId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PatientMedicalNotesDTO>> response = medicalNoteController.getMedicalNotesByPatientId(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(medicalNoteService, times(1)).getMedicalNotesByPatientId(1L);
    }

    @Test
    void deleteMedicalNote_Success() {
        doNothing().when(medicalNoteService).deleteMedicalNote(1L);

        ResponseEntity<Void> response = medicalNoteController.deleteMedicalNote(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(medicalNoteService, times(1)).deleteMedicalNote(1L);
    }

    @Test
    void deleteMedicalNote_NullId_ReturnsBadRequest() {
        ResponseEntity<Void> response = medicalNoteController.deleteMedicalNote(null);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(medicalNoteService, never()).deleteMedicalNote(any());
    }
}
