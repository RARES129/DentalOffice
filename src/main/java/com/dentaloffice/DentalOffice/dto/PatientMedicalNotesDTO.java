package com.dentaloffice.DentalOffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatientMedicalNotesDTO {
    private Long patientId;
    private String firstName;
    private String lastName;
    private List<NoteDTO> notes; // Nested list of notes

    @Getter
    @Setter
    public static class NoteDTO {
        private Long noteId;
        private String noteContent;
    }
}
