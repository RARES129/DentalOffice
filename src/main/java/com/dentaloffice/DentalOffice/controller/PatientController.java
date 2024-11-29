package com.dentaloffice.DentalOffice.controller;

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
import com.dentaloffice.DentalOffice.dto.PatientDTO;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.PatientMapper;
import com.dentaloffice.DentalOffice.service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Endpoints for managing patients.")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @Operation(
            summary = "Create a new patient",
            description = "Create a new patient by providing the necessary details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body")
            }
    )
    public ResponseEntity<PatientDTO> createPatient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient details for creation",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "dateOfBirth": "1990-01-01",
                  "email": "johndoe@example.com",
                  "phoneNumber": "1234567890"
                }
            """))
            )
            @RequestBody PatientDTO patientDTO) {
        Patient patient = patientService.savePatient(PatientMapper.toEntity(patientDTO));
        return ResponseEntity.ok(PatientMapper.toDTO(patient));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get patient by ID",
            description = "Retrieve details of a patient by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Patient not found")
            }
    )
    public ResponseEntity<PatientDTO> getPatientById(
            @Parameter(
                    description = "ID of the patient to retrieve",
                    example = "1"
            )
            @PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(PatientMapper.toDTO(patient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "Get all patients",
            description = "Retrieve a list of all registered patients.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of patients retrieved successfully")
            }
    )
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients().stream()
                .map(PatientMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update patient details",
            description = "Update details of an existing patient by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Patient not found")
            }
    )
    public ResponseEntity<PatientDTO> updatePatient(
            @Parameter(
                    description = "ID of the patient to update",
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient details for update",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                {
                  "firstName": "Rares",
                  "lastName": "Dascalu",
                  "dateOfBirth": "1990-01-01",
                  "email": "johndoe@example.com",
                  "phoneNumber": "1234567890"
                }
            """))
            )
            @RequestBody PatientDTO patientDTO) {
        Patient updatedPatient = patientService.updatePatient(id, PatientMapper.toEntity(patientDTO));
        return ResponseEntity.ok(PatientMapper.toDTO(updatedPatient));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a patient",
            description = "Delete a patient by their ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Patient deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Patient not found")
            }
    )
    public ResponseEntity<Void> deletePatient(
            @Parameter(
                    description = "ID of the patient to delete",
                    example = "1"
            )
            @PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}
