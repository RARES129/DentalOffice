
package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.entity.Patient;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
import com.dentaloffice.DentalOffice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalNoteRepository medicalNoteRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<ReportDTO> generateVisitReport() {
        return patientRepository.findAll().stream()
                .map(patient -> {
                    ReportDTO report = new ReportDTO();
                    report.setPatientId(patient.getId());
                    report.setPatientName(patient.getFirstName() + " " + patient.getLastName());

                    long visitCount = appointmentRepository.countByPatientId(patient.getId());
                    report.setVisitCount(visitCount);

                    List<MedicalNote> notes = medicalNoteRepository.findByPatientId(patient.getId());
                    List<String> noteDescriptions = notes.stream()
                            .map(MedicalNote::getNote)
                            .collect(Collectors.toList());
                    report.setPatientNotes(noteDescriptions);

                    return report;
                })
                .collect(Collectors.toList());
    }


    public ReportDTO generateVisitReportForPatient(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));

        ReportDTO report = new ReportDTO();
        report.setPatientId(patient.getId());
        report.setPatientName(patient.getFirstName() + " " + patient.getLastName());

        // Găsește toate programările pentru acest pacient
        long visitCount = appointmentRepository.countByPatientId(patientId);
        report.setVisitCount(visitCount);

        // Găsește notele medicale ale pacientului
        List<MedicalNote> notes = medicalNoteRepository.findByPatientId(patientId);
        List<String> noteDescriptions = notes.stream()
                .map(MedicalNote::getNote)
                .collect(Collectors.toList());
        report.setPatientNotes(noteDescriptions);

        return report;
    }


}
