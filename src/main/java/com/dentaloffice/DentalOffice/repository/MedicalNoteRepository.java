package com.dentaloffice.DentalOffice.repository;

import com.dentaloffice.DentalOffice.entity.MedicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalNoteRepository extends JpaRepository<MedicalNote, Long> {
    List<MedicalNote> findByPatientId(Long patientId);

    @Query("SELECT m FROM MedicalNote m JOIN FETCH m.patient p ORDER BY p.lastName ASC")
    List<MedicalNote> findAllOrderedByPatientLastName();


}
