package org.polytech.spring;

import org.springframework.stereotype.Service;

@Service
public class PatientService {

    public PatientStore patientstore;

    public PatientService(PatientStore patientstore){
        this.patientstore = patientstore;
    }

    public void savePatient(Patient p){
        patientstore.savePatient(p);
    }

}
