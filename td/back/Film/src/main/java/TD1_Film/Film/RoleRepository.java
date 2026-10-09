package TD1_Film.Film;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByFilmIdAndActeurId(Long filmId, Long acteurId);

    List<Role> findByFilmId(Long filmId);

    void deleteByFilmIdAndActeurId(Long filmId, Long acteurId);

    void deleteByFilmId(Long filmId);

    void deleteByActeurId(Long acteurId);
}
