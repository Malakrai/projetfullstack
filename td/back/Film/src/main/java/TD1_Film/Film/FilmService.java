package TD1_Film.Film;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FilmService {

    private final FilmRepository repository;

    public FilmService(FilmRepository repository) {
        this.repository = repository;
    }

    public List<Film> getFilms() {
        return repository.findAll();
    }

    public Film getFilm(Long id) {
        Film film = repository.findById(id);
        if (film == null) {
            throw new FilmNotFoundException(id);
        }
        return film;
    }

    public Film ajouterFilm(Film film) {
        repository.save(film);
        return film;
    }

    public Film modifierFilm(Long id, Film nouvellesInfos) {
        // Vérifie que le film existe avant de le modifier.
        Film film = getFilm(id);
        repository.update(id, nouvellesInfos);
        return film;
    }

    public void supprimerFilm(Long id) {
        // Vérifie que le film existe avant de le supprimer.
        getFilm(id);
        repository.deleteById(id);
    }
}