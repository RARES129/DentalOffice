package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private ReportDTO dummyReport;

    @BeforeEach
    void setUp() {
        dummyReport = new ReportDTO();
        dummyReport.setPatientId(1L);
        dummyReport.setPatientName("John Doe");
        dummyReport.setVisitCount(5L);
        dummyReport.setPatientNotes(Arrays.asList("Note 1", "Note 2"));
    }

    @Test
    void getVisitReport_Success() {
        when(reportService.generateVisitReport()).thenReturn(Arrays.asList(dummyReport));

        ResponseEntity<List<ReportDTO>> response = reportController.getVisitReport();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getPatientName());
    }

    @Test
    void getVisitReportForPatient_Success() {
        Long patientId = 1L;
        when(reportService.generateVisitReportForPatient(patientId)).thenReturn(dummyReport);

        ResponseEntity<ReportDTO> response = reportController.getVisitReportForPatient(patientId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getPatientName());
    }

    @Test
    void getVisitReportForPatient_NotFound() {
        Long patientId = 2L;
        when(reportService.generateVisitReportForPatient(patientId)).thenReturn(null);

        ResponseEntity<ReportDTO> response = reportController.getVisitReportForPatient(patientId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getVisitReportForPatient_NullId() {
        Long patientId = null;

        ResponseEntity<ReportDTO> response = reportController.getVisitReportForPatient(patientId);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}
