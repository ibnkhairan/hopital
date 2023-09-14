package fr.ael.hopital.repository;

import fr.ael.hopital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository <Patient,Long> {
//deux façons de faire
    Page<Patient> findByNomContains(String keyword, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.nom LIKE :key")
    Page<Patient> chercher(@Param("key") String keyword,Pageable pageable);
}
