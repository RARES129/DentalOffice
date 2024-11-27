package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dentaloffice.DentalOffice.service.AppointmentService;
import com.dentaloffice.DentalOffice.dto.AppointmentDTO;
import com.dentaloffice.DentalOffice.mapper.AppointmentMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        if (appointmentDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        Appointment appointment = appointmentService.saveAppointment(AppointmentMapper.toEntity(appointmentDTO, null));
        return ResponseEntity.ok(AppointmentMapper.toDTO(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return appointmentService.getAppointmentById(id)
                .map(appointment -> ResponseEntity.ok(AppointmentMapper.toDTO(appointment)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments().stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId).stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }
}
