package com.dentaloffice.DentalOffice.mapper;

import com.dentaloffice.DentalOffice.dto.AppointmentDTO;
import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;

public class AppointmentMapper {

    public static AppointmentDTO toDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
        } else {
            dto.setPatientId(null);
        }
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setReason(appointment.getReason());
        return dto;
    }

    public static Appointment toEntity(AppointmentDTO dto, Patient patient) {
        if (dto == null) {
            return null;
        }
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setPatient(patient);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        return appointment;
    }
}
