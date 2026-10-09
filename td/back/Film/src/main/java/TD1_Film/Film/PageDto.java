package TD1_Film.Film;

import java.util.List;

public record PageDto<T>(List<T> contenu, int page, int taille, long totalElements, int totalPages) {
}
