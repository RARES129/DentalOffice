package com.dentaloffice.DentalOffice.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentDTO {

    private Long id;
    @NonNull
    private Long patientId;
    @NonNull
    @FutureOrPresent(message = "The appointment date must not be in the past.")
    private LocalDate appointmentDate;
    @NonNull
    private String reason;

}
