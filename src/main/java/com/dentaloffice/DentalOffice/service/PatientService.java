package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dentaloffice.DentalOffice.repository.PatientRepository;


import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setFirstName(updatedPatient.getFirstName());
                    patient.setLastName(updatedPatient.getLastName());
                    patient.setDateOfBirth(updatedPatient.getDateOfBirth());
                    patient.setEmail(updatedPatient.getEmail());
                    patient.setPhoneNumber(updatedPatient.getPhoneNumber());
                    return patientRepository.save(patient);
                })
                .orElseGet(() -> {
                    updatedPatient.setId(id);
                    return patientRepository.save(updatedPatient);
                });
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getAllPatientsOrderedByLastName() {
        return patientRepository.findAllOrderedByLastName();
    }
}
