package TD1_Film.Film;

import org.springframework.stereotype.Component;

@Component
public class ActeurMapper {
    public ActeurDto toDto(Acteur acteur) {
        return new ActeurDto(acteur.getId(), acteur.getNom(), acteur.getPrenom());
    }

    public Acteur toEntity(ActeurCreationDto dto) {
        Acteur acteur = new Acteur();
        updateEntity(dto, acteur);
        return acteur;
    }

    public void updateEntity(ActeurCreationDto dto, Acteur acteur) {
        acteur.setNom(dto.getNom());
        acteur.setPrenom(dto.getPrenom());
    }
}
