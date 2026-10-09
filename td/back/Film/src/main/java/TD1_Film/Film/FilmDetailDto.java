package TD1_Film.Film;

import java.time.LocalDate;
import java.util.List;

public class FilmDetailDto extends FilmDto {
    private final List<ActeurDto> acteurs;
    private final List<RoleDto> roles;

    public FilmDetailDto(Long id, String titre, String realisateur, LocalDate dateSortie,
                         Genre genre, List<ActeurDto> acteurs, List<RoleDto> roles) {
        super(id, titre, realisateur, dateSortie, genre);
        this.acteurs = acteurs;
        this.roles = roles;
    }

    public List<ActeurDto> getActeurs() {
        return acteurs;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }
}
