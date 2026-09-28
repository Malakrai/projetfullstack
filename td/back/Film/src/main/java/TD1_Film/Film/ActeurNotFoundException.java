package TD1_Film.Film;

public class ActeurNotFoundException extends RuntimeException {
    public ActeurNotFoundException(Long id) {
        super("L'acteur avec l'identifiant " + id + " n'existe pas.");
    }
}
