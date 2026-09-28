package TD1_Film.Film;

import java.time.LocalDate;

public class Film {
    private Long id;
    private String titre;
    private String realisateur;
    private LocalDate dateSortie;
    private Genre genre;

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
