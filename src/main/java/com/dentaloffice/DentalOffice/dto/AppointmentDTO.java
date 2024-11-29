package com.dentaloffice.DentalOffice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class AppointmentDTO {

    private Long id;
    @NonNull
    private Long patientId;
    @NonNull
    private LocalDate appointmentDate;
    @NonNull
    private String reason;

}
