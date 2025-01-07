package com.dentaloffice.DentalOffice.mapper;

import com.dentaloffice.DentalOffice.dto.PatientMedicalNotesDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatientMedicalNotesMapper {

    public static List<PatientMedicalNotesDTO> toPatientMedicalNotesDTO(List<MedicalNote> medicalNotes) {

        Map<Long, PatientMedicalNotesDTO> patientMap = new LinkedHashMap<>();

        for (MedicalNote note : medicalNotes) {
            Long patientId = note.getPatient().getId();

            PatientMedicalNotesDTO patientDTO = patientMap.computeIfAbsent(patientId, id -> {
                PatientMedicalNotesDTO dto = new PatientMedicalNotesDTO();
                dto.setPatientId(id);
                dto.setFirstName(note.getPatient().getFirstName());
                dto.setLastName(note.getPatient().getLastName());
                dto.setNotes(new ArrayList<>());
                return dto;
            });


            PatientMedicalNotesDTO.NoteDTO noteDTO = new PatientMedicalNotesDTO.NoteDTO();
            noteDTO.setNoteId(note.getId());
            noteDTO.setNoteContent(note.getNote());
            patientDTO.getNotes().add(noteDTO);
        }

        return new ArrayList<>(patientMap.values());
    }
}
