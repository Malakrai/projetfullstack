package org.polytech.spring;

public class PatientMapper {
    public static PatientDto toDto(Patient patient) {
        return new PatientDto(patient.getId(), patient.getNom(), patient.getAge());
    }
}
