package TD1_Film.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Entity
public class Film {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 200)
    private String titre;
    private String realisateur;
    private LocalDate dateSortie;
    private Genre genre;

    // Film gere la table qui relie les films et les acteurs.
    @ManyToMany
    @JoinTable(name = "film_acteur",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "acteur_id"))
    private List<Acteur> acteurs = new ArrayList<>();

    public List<Acteur> getActeurs() {
        return acteurs;
    }

    public void ajouterActeur(Acteur acteur) {
        if (!acteurs.contains(acteur)) {
            acteurs.add(acteur);
            acteur.getFilms().add(this);
        }
    }

    public void retirerActeur(Acteur acteur) {
        acteurs.remove(acteur);
        acteur.getFilms().remove(this);
    }

    public Film() {
    }

    public Film(Long id, String titre, String realisateur,LocalDate dateSortie, Genre genre) {
        this.id = id;
        this.titre = titre;
        this.realisateur = realisateur;
        this.dateSortie = dateSortie;
        this.genre = genre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getRealisateur() { return realisateur; }
    public void setRealisateur(String realisateur) { this.realisateur = realisateur; }

    public LocalDate getDateSortie() { return dateSortie; }
    public void setDateSortie(LocalDate dateSortie) { this.dateSortie = dateSortie; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    
}
