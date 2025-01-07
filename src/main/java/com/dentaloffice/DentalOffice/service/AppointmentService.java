package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.AppointmentDTO;
import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.AppointmentMapper;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    public Appointment saveAppointment(AppointmentDTO appointmentDTO) {
        if (appointmentDTO == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        Patient patient = patientRepository.findById(appointmentDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + appointmentDTO.getPatientId()));

        Appointment appointment = AppointmentMapper.toEntity(appointmentDTO, patient);

        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        return appointmentRepository.findByPatientId(patientId);
    }

    public void deleteAppointment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        appointmentRepository.deleteById(id);
    }

    public List<AppointmentDTO> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        return appointmentRepository.findAll().stream()
                .filter(appointment -> today.equals(appointment.getAppointmentDate()))
                .map(appointment -> {
                    AppointmentDTO dto = new AppointmentDTO();
                    dto.setId(appointment.getId());
                    dto.setPatientId(appointment.getPatient().getId());
                    dto.setAppointmentDate(appointment.getAppointmentDate());
                    dto.setReason(appointment.getReason());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateAsc();
    }
}
