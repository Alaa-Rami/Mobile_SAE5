package tn.pharmacy.gestionordonnances2.entities;

public class Ordonnance {

    private String id;
    private String patientName;
    private String date;
    private String medicaments;


    public Ordonnance() {
    }


    public Ordonnance(String patientName, String date, String medicaments) {
        this.patientName = patientName;
        this.date = date;
        this.medicaments = medicaments;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(String medicaments) {
        this.medicaments = medicaments;
    }
}