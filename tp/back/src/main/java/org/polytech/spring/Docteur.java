package org.polytech.spring;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Docteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String specialite;

    @ManyToOne
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;

    @OneToMany
    @JoinTable(name = "docteur_patients",
        joinColumns = @JoinColumn(name = "docteur_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private List<Patient> patients = new ArrayList<>();

    public Docteur() {
    }

    public Docteur(String nom, String specialite) {
        this.nom = nom;
        this.specialite = specialite;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public Adresse getAdresse() { return adresse; }
    public void setAdresse(Adresse adresse) { this.adresse = adresse; }
    public List<Patient> getPatients() { return patients; }
}
