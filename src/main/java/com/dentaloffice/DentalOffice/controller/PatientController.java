package com.dentaloffice.DentalOffice.controller;

import com.dentaloffice.DentalOffice.dto.PatientDTO;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dentaloffice.DentalOffice.service.PatientService;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        Patient patient = patientService.savePatient(PatientMapper.toEntity(patientDTO));
        return ResponseEntity.ok(PatientMapper.toDTO(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(PatientMapper.toDTO(patient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients().stream()
                .map(PatientMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        Patient updatedPatient = patientService.updatePatient(id, PatientMapper.toEntity(patientDTO));
        return ResponseEntity.ok(PatientMapper.toDTO(updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}
