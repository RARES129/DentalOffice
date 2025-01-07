package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.MedicalNoteDTO;
import com.dentaloffice.DentalOffice.dto.PatientMedicalNotesDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.mapper.MedicalNoteMapper;
import com.dentaloffice.DentalOffice.service.MedicalNoteService;
import com.dentaloffice.DentalOffice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical-notes")
@Tag(name = "Medical Notes", description = "Endpoints for managing medical notes of patients")
public class MedicalNoteController {

    @Autowired
    private MedicalNoteService medicalNoteService;

    @Autowired
    private PatientService patientService;

    @PostMapping
    @Operation(summary = "Add a medical note", description = "Adds a new medical note for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical note added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalNoteDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "patientId": 1,
                                      "patientFirstName": "Rares",
                                      "patientLastName": "Dascalu",
                                      "note": "Patient diagnosed with mild gingivitis."
                                    }
                                """))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<MedicalNoteDTO> addMedicalNote(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Medical note details",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MedicalNoteDTO.class),
                    examples = @ExampleObject(value = """
                                    {
                                      "patientId": 1,
                                      "note": "Patient diagnosed with mild gingivitis."
                                    }
                                    """)))
                                                              MedicalNoteDTO medicalNoteDTO) {
        if (medicalNoteDTO == null) {
            return ResponseEntity.badRequest().body(null);  // Return 400 Bad Request directly
        }
        return patientService.getPatientById(medicalNoteDTO.getPatientId())
                .map(patient -> {
                    MedicalNote medicalNote = MedicalNoteMapper.toEntity(medicalNoteDTO, patient);
                    MedicalNote savedMedicalNote = medicalNoteService.saveMedicalNote(medicalNote);
                    return ResponseEntity.ok(MedicalNoteMapper.toDTO(savedMedicalNote));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());  // Handle patient not found
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get a medical note by ID", description = "Retrieve the details of a medical note using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical note retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalNoteDTO.class),
                            examples = @ExampleObject(value = """
                                {
                                  "id": 1,
                                  "patientId": 1,
                                  "firstName": "John",
                                  "lastName": "Doe",
                                  "note": "Patient diagnosed with mild gingivitis."
                                }
                                """))),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
            @ApiResponse(responseCode = "404", description = "Medical note not found")
    })
    public ResponseEntity<MedicalNoteDTO> getMedicalNoteById(
            @Parameter(description = "ID of the medical note", example = "1") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return medicalNoteService.getMedicalNoteById(id)
                .map(note -> ResponseEntity.ok(MedicalNoteMapper.toDTO(note)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{id}")
    @Operation(summary = "Get medical notes by patient ID", description = "Retrieve all medical notes for a specific patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical notes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                  {
                                    "id": 1,
                                    "patientId": 1,
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "note": "Patient diagnosed with mild gingivitis."
                                  },
                                """))),
            @ApiResponse(responseCode = "400", description = "Invalid patient ID provided"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<List<PatientMedicalNotesDTO>> getMedicalNotesByPatientId(
            @Parameter(description = "ID of the patient", example = "1") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();  // Directly return bad request on null id
        }
        try {
            List<PatientMedicalNotesDTO> notes = medicalNoteService.getMedicalNotesByPatientId(id);
            return ResponseEntity.ok(notes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all medical notes", description = "Retrieve all medical notes ordered by patient last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical notes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                  {
                                    "id": 1,
                                    "patientId": 1,
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "note": "Patient diagnosed with mild gingivitis."
                                  },
                                  {
                                    "id": 2,
                                    "patientId": 2,
                                    "firstName": "Jane",
                                    "lastName": "Smith",
                                    "note": "Patient shows improvement after treatment."
                                  }
                                ]
                                """))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PatientMedicalNotesDTO>> getAllMedicalNotes() {
        List<PatientMedicalNotesDTO> notes = medicalNoteService.getAllMedicalNotesOrderedByPatientLastName();
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medical note", description = "Delete a medical note by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical note deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
            @ApiResponse(responseCode = "404", description = "Medical note not found")
    })
    public ResponseEntity<Void> deleteMedicalNote(
            @Parameter(description = "ID of the medical note", example = "1") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        medicalNoteService.deleteMedicalNote(id);
        return ResponseEntity.ok().build();
    }
}
