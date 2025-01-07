package com.dentaloffice.DentalOffice.repository;

import com.dentaloffice.DentalOffice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    @Query("SELECT p FROM Patient p ORDER BY p.lastName ASC")
    List<Patient> findAllOrderedByLastName();
}
