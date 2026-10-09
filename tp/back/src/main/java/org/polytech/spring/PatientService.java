package org.polytech.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public PatientDto savePatient(PatientCreationDto dto) {
        Patient patient = new Patient(dto.nom(), dto.age());
        Patient patientSauvegarde = patientRepository.save(patient);
        return PatientMapper.toDto(patientSauvegarde);
    }

    public List<PatientDto> findPatients(String nom) {
        List<Patient> patients;
        if (nom == null) {
            patients = patientRepository.findAll();
        } else {
            patients = patientRepository.findByNom(nom);
        }

        List<PatientDto> resultat = new ArrayList<>();
        for (Patient patient : patients) {
            resultat.add(PatientMapper.toDto(patient));
        }
        return resultat;
    }

    public Optional<PatientDto> findPatient(Long id, boolean avecMedecin) {
        Optional<Patient> patient;
        if (avecMedecin) {
            patient = patientRepository.findByIdAvecMedecinTraitant(id);
        } else {
            patient = patientRepository.findById(id);
        }

        if (patient.isPresent()) {
            return Optional.of(PatientMapper.toDto(patient.get()));
        }
        return Optional.empty();
    }

}
