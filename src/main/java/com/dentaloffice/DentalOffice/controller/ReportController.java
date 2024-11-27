
package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/visits")
    public ResponseEntity<List<ReportDTO>> getVisitReport() {
        List<ReportDTO> report = reportService.generateVisitReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/visits/{patientId}")
    public ResponseEntity<ReportDTO> getVisitReportForPatient(@PathVariable Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            ReportDTO report = reportService.generateVisitReportForPatient(patientId);
            if (report != null) {
                return ResponseEntity.ok(report);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
