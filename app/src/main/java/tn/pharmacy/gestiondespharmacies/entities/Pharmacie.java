package tn.pharmacy.gestiondespharmacies.entities;

public class Pharmacie {

    private String id;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private String horaires;
    private boolean estDeGarde;

    // Constructeur par défaut
    public Pharmacie() {
    }

    // Constructeur avec tous les paramètres
    public Pharmacie(String id, String nom, String adresse, String ville, String telephone, String horaires, boolean estDeGarde) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
        this.horaires = horaires;
        this.estDeGarde = estDeGarde;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHoraires() {
        return horaires;
    }

    public void setHoraires(String horaires) {
        this.horaires = horaires;
    }

    public boolean isEstDeGarde() {
        return estDeGarde;
    }

    public void setEstDeGarde(boolean estDeGarde) {
        this.estDeGarde = estDeGarde;
    }

    // Optionnel: méthode toString pour faciliter les logs
    @Override
    public String toString() {
        return "Pharmacie{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", telephone='" + telephone + '\'' +
                ", horaires='" + horaires + '\'' +
                ", estDeGarde=" + estDeGarde +
                '}';
    }
}
