package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/appointments/{appointmentId}")
    public ResponseEntity<Void> sendAppointmentReminder(@PathVariable Long appointmentId) {
        reminderService.sendAppointmentReminder(appointmentId);
        return ResponseEntity.ok().build();
    }
}
