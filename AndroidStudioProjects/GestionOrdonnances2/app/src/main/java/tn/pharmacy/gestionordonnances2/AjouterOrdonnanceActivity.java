package tn.pharmacy.gestionordonnances2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import tn.pharmacy.gestionordonnances2.entities.Ordonnance;

public class AjouterOrdonnanceActivity extends AppCompatActivity {

    private EditText editTextPatientName, editTextDate, editTextMedicaments;
    private Button buttonSave;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_ordonnance);

        // Récupération des éléments de l'UI
        editTextPatientName = findViewById(R.id.editTextPatientName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextMedicaments = findViewById(R.id.editTextMedicaments);
        buttonSave = findViewById(R.id.buttonSave);

        // Définition du clic du bouton d'enregistrement
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterOrdonnance();
            }
        });
    }

    // Méthode pour ajouter l'ordonnance
    private void ajouterOrdonnance() {
        // Récupérer les données entrées par l'utilisateur
        String patientName = editTextPatientName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String medicaments = editTextMedicaments.getText().toString().trim();

        if (patientName.isEmpty() || date.isEmpty() || medicaments.isEmpty()) {
            Toast.makeText(AjouterOrdonnanceActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer une nouvelle ordonnance
        Ordonnance ordonnance = new Ordonnance(patientName, date, medicaments);

        // Ajouter l'ordonnance dans Firebase
        db.collection("ordonnances")
                .add(ordonnance)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AjouterOrdonnanceActivity.this, "Ordonnance ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après l'ajout
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AjouterOrdonnanceActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                });
    }
}