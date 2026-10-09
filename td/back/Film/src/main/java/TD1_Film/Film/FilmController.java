package TD1_Film.Film;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;
    private final ActeurService acteurService;

    public FilmController(FilmService service, ActeurService acteurService) {
        this.service = service;
        this.acteurService = acteurService;
    }

    @GetMapping
    public List<FilmDto> tousLesFilms() {
        return service.getFilms();
    }

    @GetMapping("/{id}")
    public FilmDetailDto unFilm(@PathVariable("id") Long id) {
        return service.getFilm(id);
    }

    @PostMapping
    public ResponseEntity<FilmDto> ajouter(@RequestBody FilmCreationDto film) {
        FilmDto nouveauFilm = service.ajouterFilm(film);
        URI adresse = URI.create("/films/" + nouveauFilm.getId());
        return ResponseEntity.created(adresse).body(nouveauFilm);
    }

    @PutMapping("/{id}")
    public FilmDto modifier(@PathVariable("id") Long id, @RequestBody FilmCreationDto film) {
        return service.modifierFilm(id, film);
    }

    @GetMapping("/{id}/acteurs")
    public List<ActeurDto> acteursFilm(@PathVariable("id") Long id,
            @RequestParam(name = "avecQuery", defaultValue = "false") boolean avecQuery) {
        return acteurService.getActeursFilm(id, avecQuery);
    }

    @PostMapping("/{filmId}/acteurs/{acteurId}")
    public ResponseEntity<Void> associer(@PathVariable("filmId") Long filmId,
                                        @PathVariable("acteurId") Long acteurId,
                                        @RequestBody(required = false) RoleSaisieDto role) {
        acteurService.associerActeur(filmId, acteurId, role == null ? null : role.personnage());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{filmId}/acteurs/{acteurId}/role")
    public ResponseEntity<Void> modifierRole(@PathVariable("filmId") Long filmId,
                                            @PathVariable("acteurId") Long acteurId,
                                            @RequestBody RoleSaisieDto role) {
        acteurService.modifierRole(filmId, acteurId, role.personnage());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{filmId}/acteurs/{acteurId}")
    public ResponseEntity<Void> dissocier(@PathVariable("filmId") Long filmId,
                                         @PathVariable("acteurId") Long acteurId) {
        acteurService.dissocierActeur(filmId, acteurId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
        service.supprimerFilm(id);
        return ResponseEntity.noContent().build();
    }
}
