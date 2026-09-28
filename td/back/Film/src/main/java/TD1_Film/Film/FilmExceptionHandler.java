package TD1_Film.Film;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FilmExceptionHandler {

    @ExceptionHandler(ActeurNotFoundException.class)
    public ProblemDetail acteurIntrouvable(ActeurNotFoundException exception) {
        ProblemDetail erreur = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage());
        erreur.setTitle("Acteur introuvable");
        return erreur;
    }

    // Transforme l'exception du service en réponse HTTP 404.
    @ExceptionHandler(FilmNotFoundException.class)
    public ProblemDetail filmIntrouvable(FilmNotFoundException exception) {
        ProblemDetail erreur = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage());
        erreur.setTitle("Film introuvable");
        return erreur;
    }
}
