package TD1_Film.Film;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FilmServiceTests {

    @Autowired
    private FilmRepository repository;
    @Autowired
    private FilmService service;
    @Autowired
    private EntityManager entityManager;
    private final FilmCreationDto infos = new FilmCreationDto(
            "Interstellar", "Christopher Nolan", LocalDate.of(2014, 11, 5), Genre.SCIENCE_FICTION);

    @Test
    void creationRetourneIdentifiantGenereEtChampsDuFilm() {
        FilmDto resultat = service.ajouterFilm(infos);

        assertNotNull(resultat.getId());
        entityManager.flush();
        entityManager.clear();
        assertTrue(repository.existsById(resultat.getId()));
        verifierChamps(resultat);
    }

    @Test
    void modificationConserveIdentifiantEtEnregistreLesNouveauxChamps() {
        FilmDto existant = service.ajouterFilm(new FilmCreationDto(
                "Ancien titre", "Ancien realisateur", null, Genre.DRAME));

        FilmDto resultat = service.modifierFilm(existant.getId(), infos);
        entityManager.flush();
        entityManager.clear();

        assertEquals(existant.getId(), resultat.getId());
        verifierChamps(resultat);
        verifierChamps(service.getFilm(existant.getId()));
    }

    @Test
    void filmAbsentDeclencheExceptionSansEcriture() {
        long nombreAvant = repository.count();

        assertThrows(FilmNotFoundException.class, () -> service.getFilm(-1L));
        assertThrows(FilmNotFoundException.class, () -> service.modifierFilm(-1L, infos));
        assertThrows(FilmNotFoundException.class, () -> service.supprimerFilm(-1L));
        assertEquals(nombreAvant, repository.count());
    }

    private void verifierChamps(FilmDto resultat) {
        assertEquals(infos.getTitre(), resultat.getTitre());
        assertEquals(infos.getRealisateur(), resultat.getRealisateur());
        assertEquals(infos.getDateSortie(), resultat.getDateSortie());
        assertEquals(infos.getGenre(), resultat.getGenre());
    }
}
