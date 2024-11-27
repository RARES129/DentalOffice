package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.MedicalNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;

import java.util.List;
import java.util.Optional;

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

    public List<MedicalNote> getMedicalNotesByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        return medicalNoteRepository.findByPatientId(patientId);
    }

    public void deleteMedicalNote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("MedicalNote ID cannot be null");
        }
        medicalNoteRepository.deleteById(id);
    }

    public List<MedicalNote> getAllMedicalNotes() {
        return medicalNoteRepository.findAll();
    }
}
