package com.dentaloffice.DentalOffice.mapper;

import com.dentaloffice.DentalOffice.dto.MedicalNoteDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;

public class MedicalNoteMapper {

    public static MedicalNoteDTO toDTO(MedicalNote medicalNote) {
        if (medicalNote == null) {
            return null;
        }
        MedicalNoteDTO dto = new MedicalNoteDTO();
        dto.setId(medicalNote.getId());
        dto.setPatientId(medicalNote.getPatient().getId());
        dto.setPatientFirstName(medicalNote.getPatient().getFirstName());
        dto.setPatientLastName(medicalNote.getPatient().getLastName());
        dto.setNote(medicalNote.getNote());
        return dto;
    }

    public static MedicalNote toEntity(MedicalNoteDTO dto, Patient patient) {
        if (dto == null) {
            return null;
        }
        MedicalNote medicalNote = new MedicalNote();
        medicalNote.setId(dto.getId());
        medicalNote.setPatient(patient);
        medicalNote.setNote(dto.getNote());
        return medicalNote;
    }
}
