package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.entity.Appointment;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicalNoteRepository medicalNoteRepository;

    @InjectMocks
    private ReportService reportService;

    private Patient dummyPatient;
    private Appointment dummyAppointment;
    private MedicalNote dummyNote;

    @BeforeEach
    void setUp() {
        dummyPatient = new Patient();
        dummyPatient.setId(1L);
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");

        dummyAppointment = new Appointment();
        dummyAppointment.setPatient(dummyPatient);

        dummyNote = new MedicalNote();
        dummyNote.setNote("Patient is recovering well.");

        lenient().when(appointmentRepository.findAll()).thenReturn(Arrays.asList(dummyAppointment));
        lenient().when(medicalNoteRepository.findByPatientId(anyLong())).thenReturn(Arrays.asList(dummyNote));
    }

    @Test
    void testGenerateVisitReport() {
        when(appointmentRepository.findAll()).thenReturn(Arrays.asList(dummyAppointment));

        List<ReportDTO> reports = reportService.generateVisitReport();

        assertNotNull(reports);
        assertFalse(reports.isEmpty());
        assertEquals(1, reports.size());
        ReportDTO report = reports.get(0);
        assertEquals(dummyPatient.getId(), report.getPatientId());
        assertEquals("John Doe", report.getPatientName());
        assertEquals(1, report.getVisitCount());
        assertEquals(1, report.getPatientNotes().size());
        assertEquals("Patient is recovering well.", report.getPatientNotes().get(0));
    }

    @Test
    void testGenerateVisitReportForPatient_Success() {
        Long patientId = 1L;
        when(appointmentRepository.findByPatientId(patientId)).thenReturn(Arrays.asList(dummyAppointment));

        ReportDTO report = reportService.generateVisitReportForPatient(patientId);

        assertNotNull(report);
        assertEquals(dummyPatient.getId(), report.getPatientId());
        assertEquals("John Doe", report.getPatientName());
        assertEquals(1, report.getVisitCount());
        assertEquals(1, report.getPatientNotes().size());
        assertEquals("Patient is recovering well.", report.getPatientNotes().get(0));
    }

    @Test
    void testGenerateVisitReportForPatient_NullId() {
        assertThrows(IllegalArgumentException.class, () -> reportService.generateVisitReportForPatient(null));
    }
}
