package TD1_Film.Film;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/acteurs")
public class ActeurController {
    private final ActeurService service;

    public ActeurController(ActeurService service) {
        this.service = service;
    }

    @GetMapping
    public List<ActeurDto> tousLesActeurs() {
        return service.getActeurs();
    }

    @GetMapping("/{id}")
    public ActeurDto unActeur(@PathVariable("id") Long id) {
        return service.getActeur(id);
    }

    @PostMapping
    public ResponseEntity<ActeurDto> ajouter(@RequestBody ActeurCreationDto acteur) {
        ActeurDto nouvelActeur = service.ajouterActeur(acteur);
        URI adresse = URI.create("/acteurs/" + nouvelActeur.getId());
        return ResponseEntity.created(adresse).body(nouvelActeur);
    }

    @PutMapping("/{id}")
    public ActeurDto modifier(@PathVariable("id") Long id, @RequestBody ActeurCreationDto acteur) {
        return service.modifierActeur(id, acteur);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
        service.supprimerActeur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/films")
    public List<FilmDto> filmsActeur(@PathVariable("id") Long id,
            @RequestParam(name = "avecQuery", defaultValue = "false") boolean avecQuery) {
        return service.getFilmsActeur(id, avecQuery);
    }
}
