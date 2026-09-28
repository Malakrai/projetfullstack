package org.polytech.spring;

import org.springframework.context.annotation.Bean;

// Configuration manuelle utilisable avec AnnotationConfigApplicationContext.
// Spring Boot utilise directement les classes @Service et @Repository.
public class AppConfig {

    // AppConfig = la classe qui configure Spring et lui explique quels objets créer et comment les relier.
    

    // Quand quelqu’un demande un PatientStore, utilise cet objet PatientDataBase.
    @Bean
    public PatientStore PatientStore(){
        return new PatientDataBase();
    }

    @Bean
    public PatientService patientservice(PatientStore patientstore){
        return new PatientService(patientstore);
    }

}


//explications
// Une méthode = comportement interne de la classe.
//Une dépendance = autre objet dont la classe a besoin pour travailler.
