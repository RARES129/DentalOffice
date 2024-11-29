package com.dentaloffice.DentalOffice.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class PatientDTO {

    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phoneNumber;

}
