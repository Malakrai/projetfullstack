package TD1_Film.Film;

import java.time.LocalDate;
import java.util.List;

public class FilmDetailDto extends FilmDto {
    private final List<ActeurDto> acteurs;

    public FilmDetailDto(Long id, String titre, String realisateur, LocalDate dateSortie,
                         Genre genre, List<ActeurDto> acteurs) {
        super(id, titre, realisateur, dateSortie, genre);
        this.acteurs = acteurs;
    }

    public List<ActeurDto> getActeurs() {
        return acteurs;
    }
}
