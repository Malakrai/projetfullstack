package TD1_Film.Film;

import org.springframework.stereotype.Component;

@Component
public class FilmMapper {
    private final ActeurMapper acteurMapper;

    public FilmMapper(ActeurMapper acteurMapper) {
        this.acteurMapper = acteurMapper;
    }

    public FilmDetailDto toDetailDto(Film film) {
        return new FilmDetailDto(film.getId(), film.getTitre(), film.getRealisateur(),
                film.getDateSortie(), film.getGenre(),
                film.getActeurs().stream().map(acteurMapper::toDto).toList());
    }

    public FilmDto toDto(Film film) {
        return new FilmDto(film.getId(), film.getTitre(), film.getRealisateur(),
                film.getDateSortie(), film.getGenre());
    }

    public Film toEntity(FilmCreationDto dto) {
        Film film = new Film();
        updateEntity(dto, film);
        return film;
    }

    public void updateEntity(FilmCreationDto dto, Film film) {
        film.setTitre(dto.getTitre());
        film.setRealisateur(dto.getRealisateur());
        film.setDateSortie(dto.getDateSortie());
        film.setGenre(dto.getGenre());
    }
}
