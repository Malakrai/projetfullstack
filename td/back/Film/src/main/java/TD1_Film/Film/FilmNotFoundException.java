package TD1_Film.Film;


public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(Long id) {
        super("Le film avec l'identifiant " + id + " n'existe pas.");
    }
}