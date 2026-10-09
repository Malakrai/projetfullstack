package TD1_Film.Film;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilmService {

    private final FilmRepository repository;
    private final FilmMapper mapper;

    public FilmService(FilmRepository repository, FilmMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FilmDto> getFilms() {
        List<FilmDto> resultat = new ArrayList<>();
        for (Film film : repository.findAll()) {
            resultat.add(mapper.toDto(film));
        }
        return resultat;
    }

    public FilmDetailDto getFilm(Long id) {
        Film film = trouverFilm(id);
        return mapper.toDetailDto(film);
    }

    private Film trouverFilm(Long id) {
        Optional<Film> resultat = repository.findById(id);
        if (resultat.isEmpty()) {
            throw new FilmNotFoundException(id);
        }
        return resultat.get();
    }

    public FilmDto ajouterFilm(FilmCreationDto dto) {
        Film film = mapper.toEntity(dto);
        Film filmEnregistre = repository.save(film);
        return mapper.toDto(filmEnregistre);
    }

    public FilmDto modifierFilm(Long id, FilmCreationDto nouvellesInfos) {
        Film film = trouverFilm(id);
        mapper.updateEntity(nouvellesInfos, film);
        Film filmEnregistre = repository.save(film);
        return mapper.toDto(filmEnregistre);
    }

    public void supprimerFilm(Long id) {
        Film film = trouverFilm(id);
        // On retire les liens sans supprimer les acteurs.
        for (Acteur acteur : new ArrayList<>(film.getActeurs())) {
            film.retirerActeur(acteur);
        }
        repository.delete(film);
    }
}
