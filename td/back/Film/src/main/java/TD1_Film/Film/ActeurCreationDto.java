package TD1_Film.Film;

public class ActeurCreationDto {
    private String nom;
    private String prenom;

    public ActeurCreationDto() {
    }

    public ActeurCreationDto(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
