package TD1_Film.Film;

import java.time.LocalDate;

public class FilmCreationDto {
    private String titre;
    private String realisateur;
    private LocalDate dateSortie;
    private Genre genre;

    public FilmCreationDto() {
    }

    public FilmCreationDto(String titre, String realisateur, LocalDate dateSortie, Genre genre) {
        this.titre = titre;
        this.realisateur = realisateur;
        this.dateSortie = dateSortie;
        this.genre = genre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
