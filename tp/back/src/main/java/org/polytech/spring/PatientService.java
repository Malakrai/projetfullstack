package org.polytech.spring;

public class PatientService {

    public PatientStore patientstore;

    public PatientService(PatientStore patientstore){
        this.patientstore = patientstore;
    }

    public void savePatient(Patient p){
        patientstore.savePatient(p);
    }

}
