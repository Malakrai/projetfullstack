package org.polytech.spring;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByNom(String nom);

    @Query("select p from Patient p left join fetch p.medecinTraitant where p.id = :id")
    Optional<Patient> findByIdAvecMedecinTraitant(@Param("id") Long id);
}
