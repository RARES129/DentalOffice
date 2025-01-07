package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
import com.dentaloffice.DentalOffice.repository.PatientRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicalNoteRepository medicalNoteRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private ReportService reportService;

    private Patient dummyPatient;
    private MedicalNote dummyNote;

    @BeforeEach
    void setUp() {
        dummyPatient = new Patient();
        dummyPatient.setId(1L);
        dummyPatient.setFirstName("John");
        dummyPatient.setLastName("Doe");

        dummyNote = new MedicalNote();
        dummyNote.setNote("Patient is recovering well.");

        // Mock repository behaviors
        when(patientRepository.findAll()).thenReturn(Arrays.asList(dummyPatient));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(dummyPatient));
        when(appointmentRepository.countByPatientId(1L)).thenReturn(1L);
        when(medicalNoteRepository.findByPatientId(1L)).thenReturn(Arrays.asList(dummyNote));
    }

    @Test
    void testGenerateVisitReport() {
        List<ReportDTO> reports = reportService.generateVisitReport();

        assertNotNull(reports);
        assertFalse(reports.isEmpty());
        assertEquals(1, reports.size());

        ReportDTO report = reports.get(0);
        assertEquals(1L, report.getPatientId());
        assertEquals("John Doe", report.getPatientName());
        assertEquals(1L, report.getVisitCount());
        assertEquals(1, report.getPatientNotes().size());
        assertEquals("Patient is recovering well.", report.getPatientNotes().get(0));
    }

    @Test
    void testGenerateVisitReportForPatient_Success() {
        Long patientId = 1L;
        ReportDTO report = reportService.generateVisitReportForPatient(patientId);

        assertNotNull(report);
        assertEquals(1L, report.getPatientId());
        assertEquals("John Doe", report.getPatientName());
        assertEquals(1L, report.getVisitCount());
        assertEquals(1, report.getPatientNotes().size());
        assertEquals("Patient is recovering well.", report.getPatientNotes().get(0));
    }

    @Test
    void testGenerateVisitReportForPatient_NullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                reportService.generateVisitReportForPatient(null)
        );
        assertEquals("Patient ID cannot be null", exception.getMessage());
    }
}
