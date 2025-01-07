package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private ReminderService reminderService;

    private Appointment dummyAppointment;
    private Patient dummyPatient;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        dummyPatient = new Patient();
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");
        dummyPatient.setEmail("john.doe@example.com");

        dummyAppointment = new Appointment();
        dummyAppointment.setId(1L);
        dummyAppointment.setPatient(dummyPatient);
        dummyAppointment.setAppointmentDate(LocalDate.of(2022, 9, 12));
    }

    @Test
    void sendAppointmentReminder_Success() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(dummyAppointment));

        reminderService.sendAppointmentReminder(1L);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAppointmentReminder_AppointmentNotFound() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reminderService.sendAppointmentReminder(1L);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

}
