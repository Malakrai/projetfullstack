package org.polytech.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public class PatientDataBase implements PatientStore {
    
    private List<Patient> patients = new ArrayList<>();

    @Override
    public void savePatient(Patient p){
        patients.add(p);
        System.out.println("Le patient est bien enregistré");

    }
}
