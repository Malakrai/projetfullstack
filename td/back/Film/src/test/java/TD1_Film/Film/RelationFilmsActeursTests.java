package TD1_Film.Film;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RelationFilmsActeursTests {
    @Autowired
    private FilmService filmService;
    @Autowired
    private ActeurService acteurService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ActeurRepository acteurRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void associationEtDissociationSontEnregistreesDansLesDeuxSens() {
        FilmDto film = filmService.ajouterFilm(
                new FilmCreationDto("Blade Runner", "Ridley Scott", null, Genre.SCIENCE_FICTION));
        ActeurDto acteur = acteurService.ajouterActeur(new ActeurCreationDto("Ford", "Harrison"));

        acteurService.associerActeur(film.getId(), acteur.getId());
        acteurService.associerActeur(film.getId(), acteur.getId());
        rechargerDepuisLaBase();

        assertEquals(1, acteurService.getActeursFilm(film.getId(), false).size());
        assertEquals(acteur.getId(), acteurService.getActeursFilm(film.getId(), true).get(0).getId());
        assertEquals(1, acteurService.getFilmsActeur(acteur.getId(), false).size());
        assertEquals(film.getId(), acteurService.getFilmsActeur(acteur.getId(), true).get(0).getId());

        FilmDetailDto detail = filmService.getFilm(film.getId());
        assertEquals("Blade Runner", detail.getTitre());
        assertEquals(1, detail.getActeurs().size());
        assertEquals(acteur.getId(), detail.getActeurs().get(0).getId());
        assertEquals("Ford", detail.getActeurs().get(0).getNom());

        acteurService.dissocierActeur(film.getId(), acteur.getId());
        acteurService.dissocierActeur(film.getId(), acteur.getId());
        rechargerDepuisLaBase();

        assertTrue(acteurService.getActeursFilm(film.getId(), false).isEmpty());
        assertTrue(acteurService.getFilmsActeur(acteur.getId(), true).isEmpty());
        assertTrue(filmService.getFilm(film.getId()).getActeurs().isEmpty());
        assertTrue(filmRepository.existsById(film.getId()));
        assertTrue(acteurRepository.existsById(acteur.getId()));
    }

    @Test
    void supprimerUnActeurConserveSesFilms() {
        FilmDto film = filmService.ajouterFilm(new FilmCreationDto("Film test", "Test", null, Genre.DRAME));
        ActeurDto acteur = acteurService.ajouterActeur(new ActeurCreationDto("Nom", "Prenom"));
        acteurService.associerActeur(film.getId(), acteur.getId());
        rechargerDepuisLaBase();

        acteurService.supprimerActeur(acteur.getId());
        rechargerDepuisLaBase();

        assertFalse(acteurRepository.existsById(acteur.getId()));
        assertTrue(filmRepository.existsById(film.getId()));
        assertTrue(acteurService.getActeursFilm(film.getId(), false).isEmpty());
    }

    @Test
    void supprimerUnFilmConserveSesActeurs() {
        FilmDto film = filmService.ajouterFilm(new FilmCreationDto("Film test", "Test", null, Genre.DRAME));
        ActeurDto acteur = acteurService.ajouterActeur(new ActeurCreationDto("Nom", "Prenom"));
        acteurService.associerActeur(film.getId(), acteur.getId());
        rechargerDepuisLaBase();

        filmService.supprimerFilm(film.getId());
        rechargerDepuisLaBase();

        assertFalse(filmRepository.existsById(film.getId()));
        assertTrue(acteurRepository.existsById(acteur.getId()));
        assertTrue(acteurService.getFilmsActeur(acteur.getId(), false).isEmpty());
    }

    private void rechargerDepuisLaBase() {
        entityManager.flush();
        entityManager.clear();
    }
}
