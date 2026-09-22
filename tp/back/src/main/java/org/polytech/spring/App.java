package org.polytech.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@SpringBootApplication
public class App {


    public static void main(String[] args) {
        
        try (AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class)) {

        PatientService patientService =
                context.getBean(PatientService.class);

       //new patient
       Patient patient = new Patient("Malak", 22);

       patientService.savePatient(patient);

        }




        //SpringApplication.run(App.class, args);

    }
}
