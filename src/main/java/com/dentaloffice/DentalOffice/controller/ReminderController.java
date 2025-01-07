package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
@Tag(name = "Reminders", description = "Endpoints for sending appointment reminders")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/appointments/{appointmentId}")
    @Operation(
            summary = "Send a reminder for an appointment",
            description = "Send an email reminder to the patient for the specified appointment."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder sent successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid appointment ID")
    })
    public ResponseEntity<Void> sendAppointmentReminder(
            @Parameter(description = "ID of the appointment", example = "1") @PathVariable Long appointmentId) {
        reminderService.sendAppointmentReminder(appointmentId);
        return ResponseEntity.ok().build();
    }
}
