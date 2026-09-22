package org.polytech.spring;

import org.springframework.context.annotation.Primary;


@Primary
public class PatientSerialization implements PatientStore{

    public void savePatient(Patient p){

    }

}
