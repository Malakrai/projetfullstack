package TD1_Film.Film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    // Spring construit la requete a partir du nom de la methode.
    List<Film> findByActeursId(Long acteurId);

    // La meme recherche, ecrite en JPQL avec les noms des classes et des champs.
    @Query("select f from Film f join f.acteurs a where a.id = :acteurId")
    List<Film> trouverFilmsParActeur(@Param("acteurId") Long acteurId);
}
