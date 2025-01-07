package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.MedicalNoteDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.mapper.PatientMedicalNotesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
import com.dentaloffice.DentalOffice.dto.PatientMedicalNotesDTO;

import java.util.*;

@Service
public class MedicalNoteService {

    @Autowired
    private MedicalNoteRepository medicalNoteRepository;

    public MedicalNote saveMedicalNote(MedicalNote medicalNote) {
        if (medicalNote == null) {
            throw new IllegalArgumentException("MedicalNote cannot be null");
        }
        return medicalNoteRepository.save(medicalNote);
    }

    public Optional<MedicalNote> getMedicalNoteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("MedicalNote ID cannot be null");
        }
        return medicalNoteRepository.findById(id);
    }

    public List<PatientMedicalNotesDTO> getMedicalNotesByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        List<MedicalNote> medicalNotes = medicalNoteRepository.findByPatientId(patientId);
        return PatientMedicalNotesMapper.toPatientMedicalNotesDTO(medicalNotes);
    }

    public void deleteMedicalNote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("MedicalNote ID cannot be null");
        }
        medicalNoteRepository.deleteById(id);
    }

    public List<PatientMedicalNotesDTO> getAllMedicalNotesOrderedByPatientLastName() {
        List<MedicalNote> medicalNotes = medicalNoteRepository.findAllOrderedByPatientLastName();
        return PatientMedicalNotesMapper.toPatientMedicalNotesDTO(medicalNotes);
    }
}
