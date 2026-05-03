package cinema.BO;

public class Salle {

    private int idSalle;
    private int numSalle;
    private String descSalle;
    private String particularite;
    private int nbPlaces;
    private int idCinema;

    public Salle() {
    }

    public Salle(int idSalle, int numSalle, String descSalle, String particularite, int nbPlaces, int idCinema) {
        this.idSalle = idSalle;
        this.numSalle = numSalle;
        this.descSalle = descSalle;
        this.particularite = particularite;
        this.nbPlaces = nbPlaces;
        this.idCinema = idCinema;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public int getNumSalle() {
        return numSalle;
    }

    public void setNumSalle(int numSalle) {
        this.numSalle = numSalle;
    }

    public String getDescSalle() {
        return descSalle;
    }

    public void setDescSalle(String descSalle) {
        this.descSalle = descSalle;
    }

    public String getParticularite() {
        return particularite;
    }

    public void setParticularite(String particularite) {
        this.particularite = particularite;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public int getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(int idCinema) {
        this.idCinema = idCinema;
    }

    @Override
    public String toString() {
        return "Salle [idSalle=" + idSalle + ", numSalle=" + numSalle + ", descSalle=" + descSalle + ", nbPlaces=" + nbPlaces + ", idCinema=" + idCinema + "]";
    }
}
