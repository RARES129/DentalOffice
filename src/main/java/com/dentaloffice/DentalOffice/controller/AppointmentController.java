package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.entity.Appointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Appointments", description = "Endpoints for managing appointments in the dental office")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    @Operation(
            summary = "Create a new appointment",
            description = "Create a new appointment by providing appointment details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body")
            }
    )
    public ResponseEntity<AppointmentDTO> createAppointment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the appointment to create",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "patientId": 1,
                                              "appointmentDate": "2024-12-01",
                                              "reason": "Routine dental check-up"
                                            }
                                            """
                            )
                    )
            ) @RequestBody AppointmentDTO appointmentDTO) {
//        System.out.println(appointmentDTO.getPatientId()+" "+appointmentDTO.getAppointmentDate()+" "+appointmentDTO.getReason());
        if (appointmentDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Appointment appointment = appointmentService.saveAppointment(appointmentDTO);
            return ResponseEntity.ok(AppointmentMapper.toDTO(appointment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieve details of an appointment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Appointment not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid appointment ID")
            }
    )
    public ResponseEntity<AppointmentDTO> getAppointmentById(
            @Parameter(description = "ID of the appointment to retrieve", example = "1") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        return appointmentService.getAppointmentById(id)
                .map(appointment -> ResponseEntity.ok(AppointmentMapper.toDTO(appointment)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "Get all appointments",
            description = "Retrieve a list of all appointments.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully")
            }
    )
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments().stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(
            summary = "Get appointments for a specific patient",
            description = "Retrieve a list of appointments for a patient identified by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid patient ID")
            }
    )
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatientId(
            @Parameter(description = "ID of the patient whose appointments are to be retrieved", example = "1") @PathVariable Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId).stream()
                .map(AppointmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an appointment",
            description = "Delete an appointment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Appointment not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid appointment ID")
            }
    )
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID of the appointment to delete", example = "1") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }
}
