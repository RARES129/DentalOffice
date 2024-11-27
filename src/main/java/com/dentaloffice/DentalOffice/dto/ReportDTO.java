package com.dentaloffice.DentalOffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportDTO {
    private Long patientId;
    private String patientName;
    private Long visitCount;
    private List<String> patientNotes;
}
