
package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.service.ReportService;
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

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints for generating visit reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/visits")
    @Operation(
            summary = "Get a report of all patient visits",
            description = "Returns a list of visit reports for all patients.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report generated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReportDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<List<ReportDTO>> getVisitReport() {
        List<ReportDTO> report = reportService.generateVisitReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/visits/{patientId}")
    @Operation(
            summary = "Get a report for a specific patient",
            description = "Returns a visit report for a patient identified by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report generated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReportDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid patient ID",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"Patient ID cannot be null or invalid\"}"))),
                    @ApiResponse(responseCode = "404", description = "Patient not found",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"Patient not found\"}"))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ReportDTO> getVisitReportForPatient(@Parameter(description = "The ID of the patient", example = "1") @PathVariable Long patientId) {
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
