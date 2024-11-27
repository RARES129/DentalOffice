package com.dentaloffice.DentalOffice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalNoteDTO {

    private Long id;
    @NotNull
    private Long patientId;
    @NotNull
    private String note;
}
