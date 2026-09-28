package TD1_Film.Film;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> tousLesFilms() {
        return service.getFilms();
    }

    @GetMapping("/{id}")
    public Film unFilm(@PathVariable("id") Long id) {
        return service.getFilm(id);
    }

    @PostMapping
    public ResponseEntity<Film> ajouter(@RequestBody Film film) {
        Film nouveauFilm = service.ajouterFilm(film);
        URI adresse = URI.create("/films/" + nouveauFilm.getId());
        return ResponseEntity.created(adresse).body(nouveauFilm);
    }

    @PutMapping("/{id}")
    public Film modifier(@PathVariable("id") Long id, @RequestBody Film film) {
        return service.modifierFilm(id, film);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
        service.supprimerFilm(id);
        return ResponseEntity.noContent().build();
    }
}