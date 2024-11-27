package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.MedicalNoteDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.mapper.MedicalNoteMapper;
import com.dentaloffice.DentalOffice.service.MedicalNoteService;
import com.dentaloffice.DentalOffice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-notes")
public class MedicalNoteController {

    @Autowired
    private MedicalNoteService medicalNoteService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<MedicalNoteDTO> addMedicalNote(@RequestBody MedicalNoteDTO medicalNoteDTO) {
        if (medicalNoteDTO == null) {
            return ResponseEntity.badRequest().body(null);  // Return 400 Bad Request directly
        }
        return patientService.getPatientById(medicalNoteDTO.getPatientId())
                .map(patient -> {
                    MedicalNote medicalNote = MedicalNoteMapper.toEntity(medicalNoteDTO, patient);
                    MedicalNote savedMedicalNote = medicalNoteService.saveMedicalNote(medicalNote);
                    return ResponseEntity.ok(MedicalNoteMapper.toDTO(savedMedicalNote));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());  // Handle patient not found
    }



    @GetMapping("/{id}")
    public ResponseEntity<MedicalNoteDTO> getMedicalNoteById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return medicalNoteService.getMedicalNoteById(id)
                .map(note -> ResponseEntity.ok(MedicalNoteMapper.toDTO(note)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<MedicalNoteDTO>> getMedicalNotesByPatientId(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();  // Directly return bad request on null id
        }
        try {
            List<MedicalNote> medicalNotes = medicalNoteService.getMedicalNotesByPatientId(id);
            List<MedicalNoteDTO> medicalNoteDTOs = medicalNotes.stream()
                    .map(MedicalNoteMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(medicalNoteDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalNoteDTO>> getAllMedicalNotes() {
        List<MedicalNoteDTO> notes = medicalNoteService.getAllMedicalNotes().stream()
                .map(note -> {
                    if (note == null) {
                        throw new IllegalStateException("Note is null during mapping");
                    }
                    return MedicalNoteMapper.toDTO(note);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalNote(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        medicalNoteService.deleteMedicalNote(id);
        return ResponseEntity.ok().build();
    }
}
