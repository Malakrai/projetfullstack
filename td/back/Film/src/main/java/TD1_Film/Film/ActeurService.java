package TD1_Film.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// Une transaction regroupe les lectures et les modifications de la relation.
@Transactional
public class ActeurService {
    private final ActeurRepository repository;
    private final FilmRepository filmRepository;
    private final ActeurMapper mapper;
    private final FilmMapper filmMapper;

    public ActeurService(ActeurRepository repository, FilmRepository filmRepository,
                         ActeurMapper mapper, FilmMapper filmMapper) {
        this.repository = repository;
        this.filmRepository = filmRepository;
        this.mapper = mapper;
        this.filmMapper = filmMapper;
    }

    private Acteur trouverActeur(Long id) {
        Optional<Acteur> resultat = repository.findById(id);
        if (resultat.isEmpty()) {
            throw new ActeurNotFoundException(id);
        }
        return resultat.get();
    }

    private Film trouverFilm(Long id) {
        Optional<Film> resultat = filmRepository.findById(id);
        if (resultat.isEmpty()) {
            throw new FilmNotFoundException(id);
        }
        return resultat.get();
    }

    public List<ActeurDto> getActeurs() {
        List<ActeurDto> resultat = new ArrayList<>();
        for (Acteur acteur : repository.findAll()) {
            resultat.add(mapper.toDto(acteur));
        }
        return resultat;
    }

    public ActeurDto getActeur(Long id) {
        Acteur acteur = trouverActeur(id);
        return mapper.toDto(acteur);
    }

    public ActeurDto ajouterActeur(ActeurCreationDto dto) {
        Acteur acteur = mapper.toEntity(dto);
        Acteur acteurEnregistre = repository.save(acteur);
        return mapper.toDto(acteurEnregistre);
    }

    public ActeurDto modifierActeur(Long id, ActeurCreationDto dto) {
        Acteur acteur = trouverActeur(id);
        mapper.updateEntity(dto, acteur);
        Acteur acteurEnregistre = repository.save(acteur);
        return mapper.toDto(acteurEnregistre);
    }

    public void supprimerActeur(Long id) {
        Acteur acteur = trouverActeur(id);
        // Une copie permet de parcourir les films tout en retirant les liens.
        for (Film film : new ArrayList<>(acteur.getFilms())) {
            film.retirerActeur(acteur);
            filmRepository.save(film);
        }
        repository.delete(acteur);
    }

    public void associerActeur(Long filmId, Long acteurId) {
        Film film = trouverFilm(filmId);
        Acteur acteur = trouverActeur(acteurId);
        film.ajouterActeur(acteur);
        filmRepository.save(film);
    }

    public void dissocierActeur(Long filmId, Long acteurId) {
        Film film = trouverFilm(filmId);
        Acteur acteur = trouverActeur(acteurId);
        film.retirerActeur(acteur);
        filmRepository.save(film);
    }

    public List<FilmDto> getFilmsActeur(Long acteurId, boolean avecQuery) {
        trouverActeur(acteurId);
        List<Film> films;
        if (avecQuery) {
            films = filmRepository.trouverFilmsParActeur(acteurId);
        } else {
            films = filmRepository.findByActeursId(acteurId);
        }
        List<FilmDto> resultat = new ArrayList<>();
        for (Film film : films) {
            resultat.add(filmMapper.toDto(film));
        }
        return resultat;
    }

    public List<ActeurDto> getActeursFilm(Long filmId, boolean avecQuery) {
        trouverFilm(filmId);
        List<Acteur> acteurs;
        if (avecQuery) {
            acteurs = repository.trouverActeursParFilm(filmId);
        } else {
            acteurs = repository.findByFilmsId(filmId);
        }
        List<ActeurDto> resultat = new ArrayList<>();
        for (Acteur acteur : acteurs) {
            resultat.add(mapper.toDto(acteur));
        }
        return resultat;
    }
}
