package org.polytech.spring;

public class Patient {

    private String nom;
    private int age;


    public Patient(String nom, int age){
        this.nom=nom;
        this.age=age;
    }
    public String getNom(String nom){
        return nom;
    }

    public int getAge(int age){
        return age;
    }

}
