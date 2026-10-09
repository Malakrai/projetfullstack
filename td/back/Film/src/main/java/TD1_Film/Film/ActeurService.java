package TD1_Film.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
// Une transaction regroupe les lectures et les modifications de la relation.
@Transactional
public class ActeurService {
    private static final Set<String> TRIS_AUTORISES = Set.of("id", "nom", "prenom");

    private final ActeurRepository repository;
    private final FilmRepository filmRepository;
    private final ActeurMapper mapper;
    private final FilmMapper filmMapper;
    private final RoleRepository roleRepository;

    public ActeurService(ActeurRepository repository, FilmRepository filmRepository,
                         ActeurMapper mapper, FilmMapper filmMapper, RoleRepository roleRepository) {
        this.repository = repository;
        this.filmRepository = filmRepository;
        this.mapper = mapper;
        this.filmMapper = filmMapper;
        this.roleRepository = roleRepository;
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

    public PageDto<ActeurDto> getPageActeurs(int page, int taille, String tri, String sens) {
        String champ = TRIS_AUTORISES.contains(tri) ? tri : "nom";
        Sort.Direction direction = "desc".equalsIgnoreCase(sens) ? Sort.Direction.DESC : Sort.Direction.ASC;
        int pageValide = Math.max(page, 0);
        int tailleValide = Math.min(Math.max(taille, 1), 50);
        Page<Acteur> resultat = repository.findAll(
                PageRequest.of(pageValide, tailleValide, Sort.by(direction, champ).and(Sort.by("id"))));
        List<ActeurDto> contenu = new ArrayList<>();
        for (Acteur acteur : resultat.getContent()) {
            contenu.add(mapper.toDto(acteur));
        }
        return new PageDto<>(contenu, resultat.getNumber(), resultat.getSize(),
                resultat.getTotalElements(), resultat.getTotalPages());
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
        roleRepository.deleteByActeurId(id);
        // Une copie permet de parcourir les films tout en retirant les liens.
        for (Film film : new ArrayList<>(acteur.getFilms())) {
            film.retirerActeur(acteur);
            filmRepository.save(film);
        }
        repository.delete(acteur);
    }

    public void associerActeur(Long filmId, Long acteurId) {
        associerActeur(filmId, acteurId, null);
    }

    public void associerActeur(Long filmId, Long acteurId, String personnage) {
        Film film = trouverFilm(filmId);
        Acteur acteur = trouverActeur(acteurId);
        film.ajouterActeur(acteur);
        filmRepository.save(film);
        enregistrerRole(film, acteur, personnage);
    }

    public void modifierRole(Long filmId, Long acteurId, String personnage) {
        Film film = trouverFilm(filmId);
        Acteur acteur = trouverActeur(acteurId);
        if (!film.getActeurs().contains(acteur)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "L'acteur " + acteurId + " ne joue pas dans le film " + filmId);
        }
        enregistrerRole(film, acteur, personnage);
    }

    private void enregistrerRole(Film film, Acteur acteur, String personnage) {
        Optional<Role> existant = roleRepository.findByFilmIdAndActeurId(film.getId(), acteur.getId());
        if (personnage == null || personnage.isBlank()) {
            existant.ifPresent(roleRepository::delete);
            return;
        }
        Role role = existant.orElseGet(() -> new Role(film, acteur, null));
        role.setPersonnage(personnage.trim());
        roleRepository.save(role);
    }

    public void dissocierActeur(Long filmId, Long acteurId) {
        Film film = trouverFilm(filmId);
        Acteur acteur = trouverActeur(acteurId);
        roleRepository.deleteByFilmIdAndActeurId(filmId, acteurId);
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
