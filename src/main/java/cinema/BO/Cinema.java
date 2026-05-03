package cinema.BO;

public class Cinema {

    private int idCinema;
    private String denomination;
    private String adresse;
    private String ville;
    private int idFranchise;
    private String nomFranchise;

    public Cinema(int idCinema, String denomination, String adresse, String ville, int idFranchise) {
        this.idCinema = idCinema;
        this.denomination = denomination;
        this.adresse = adresse;
        this.ville = ville;
        this.idFranchise = idFranchise;
    }

    public Cinema(String ville){
        this.ville = ville;
    }


    public int getIdCinema() {
        return idCinema;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getIdFranchise() {
        return idFranchise;
    }

    public void setIdFranchise(int idFranchise) {
        this.idFranchise = idFranchise;
    }

    public void setNomFranchise(String nomFranchise) {
        this.nomFranchise = nomFranchise;
    }

    public String getNomFranchise() {
        return nomFranchise;
    }

    public String toString(){
        //si la dénomination est pas nulle c qu'on est dans la page "Ajouter Salle"
        if (this.denomination != null && !this.denomination.isEmpty()) {
            return this.denomination + " (" + this.ville + ")";
        }

        //sinon c'est qu'on a créé l'objet juste avec la ville pour la page "Ajouter Cinéma"
        return this.ville;
    }
}
