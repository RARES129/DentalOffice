package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentReminder(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getPatient().getEmail());
        message.setSubject("Appointment Reminder");
        message.setText("Dear " + appointment.getPatient().getFirstName() +
                ",\n\nThis is a reminder for your appointment on " + appointment.getAppointmentDate() +
                ".\n\nBest regards,\nDental Office");
        mailSender.send(message);
    }
}
