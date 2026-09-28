package TD1_Film.Film;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FilmRepository {

    
    private final List<Film> films = new ArrayList<>();
    private long prochainId = 1;

    public List<Film> findAll() {
        return films;
    }

    public Film findById(Long id) {
        for (Film film : films) {
            if (film.getId().equals(id)) {
                return film;
            }
        }
        return null;
    }

    public void save(Film film) {
        
        film.setId(prochainId);
        prochainId++;
        films.add(film);
    }

    public void update(Long id, Film nouvellesInfos) {
        Film film = findById(id);
        if (film != null) {
            film.setTitre(nouvellesInfos.getTitre());
            film.setRealisateur(nouvellesInfos.getRealisateur());
            film.setDateSortie(nouvellesInfos.getDateSortie());
            film.setGenre(nouvellesInfos.getGenre());
        }
    }

    public void deleteById(Long id) {
        Film film = findById(id);
        if (film != null) {
            films.remove(film);
        }
    }
}