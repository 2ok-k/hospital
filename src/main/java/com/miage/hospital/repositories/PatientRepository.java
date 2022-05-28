package com.miage.hospital.repositories;

import com.miage.hospital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Page<Patient> findByNomContains(String kw, Pageable pageable); //Une méthode qui permet d'envoyer une page de patients trié à partir du nom contenant le mot clé "keyword"
}
