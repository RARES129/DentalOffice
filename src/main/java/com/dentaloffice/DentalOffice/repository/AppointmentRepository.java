package com.dentaloffice.DentalOffice.repository;

import com.dentaloffice.DentalOffice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);
    Long countByPatientId(Long patientId);
    List<Appointment> findAllByOrderByAppointmentDateAsc();
}
