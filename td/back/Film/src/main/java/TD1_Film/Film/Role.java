package TD1_Film.Film;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "film_role", uniqueConstraints = @UniqueConstraint(columnNames = {"film_id", "acteur_id"}))
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(optional = false)
    @JoinColumn(name = "acteur_id")
    private Acteur acteur;

    private String personnage;

    public Role() {
    }

    public Role(Film film, Acteur acteur, String personnage) {
        this.film = film;
        this.acteur = acteur;
        this.personnage = personnage;
    }

    public Long getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public Acteur getActeur() {
        return acteur;
    }

    public String getPersonnage() {
        return personnage;
    }

    public void setPersonnage(String personnage) {
        this.personnage = personnage;
    }
}
