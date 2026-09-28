package TD1_Film.Film;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Acteur {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String nom;
    private String prenom;

    // "acteurs" est le nom du champ dans la classe Film.
    @ManyToMany(mappedBy = "acteurs")
    private List<Film> films = new ArrayList<>();

    public Acteur() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<Film> getFilms() {
        return films;
    }
}
