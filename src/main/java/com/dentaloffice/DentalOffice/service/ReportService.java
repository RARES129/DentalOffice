
package com.dentaloffice.DentalOffice.service;

import com.dentaloffice.DentalOffice.dto.ReportDTO;
import com.dentaloffice.DentalOffice.entity.MedicalNote;
import com.dentaloffice.DentalOffice.repository.AppointmentRepository;
import com.dentaloffice.DentalOffice.repository.MedicalNoteRepository;
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

    public List<ReportDTO> generateVisitReport() {
        return appointmentRepository.findAll().stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getPatient().getId()))
                .entrySet().stream()
                .map(entry -> {
                    ReportDTO report = new ReportDTO();
                    report.setPatientId(entry.getKey());
                    report.setPatientName(entry.getValue().get(0).getPatient().getFirstName() + " " + entry.getValue().get(0).getPatient().getLastName());
                    report.setVisitCount((long) entry.getValue().size());

                    List<MedicalNote> notes = medicalNoteRepository.findByPatientId(entry.getKey());
                    List<String> noteDescriptions = notes.stream()
                            .map(MedicalNote::getNote)  // Assuming a getNote() method exists
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

        List<MedicalNote> notes = medicalNoteRepository.findByPatientId(patientId);
        return appointmentRepository.findByPatientId(patientId).stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getPatient().getId()))
                .entrySet().stream()
                .map(entry -> {
                    ReportDTO report = new ReportDTO();
                    report.setPatientId(entry.getKey());
                    report.setPatientName(entry.getValue().get(0).getPatient().getFirstName() + " " + entry.getValue().get(0).getPatient().getLastName());
                    report.setVisitCount((long) entry.getValue().size());
                    report.setPatientNotes(notes.stream().map(MedicalNote::getNote).collect(Collectors.toList()));
                    return report;
                })
                .findFirst().orElse(null);
    }

}
