package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment saveAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
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

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
