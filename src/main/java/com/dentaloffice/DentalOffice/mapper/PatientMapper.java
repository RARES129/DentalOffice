package com.dentaloffice.DentalOffice.mapper;

import com.dentaloffice.DentalOffice.dto.PatientDTO;
import com.dentaloffice.DentalOffice.entity.Patient;

public class PatientMapper {

    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        return dto;
    }

    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        return patient;
    }
}
