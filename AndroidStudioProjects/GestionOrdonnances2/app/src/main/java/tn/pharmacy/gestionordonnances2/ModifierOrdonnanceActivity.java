package tn.pharmacy.gestionordonnances2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import tn.pharmacy.gestionordonnances2.entities.Ordonnance;

public class ModifierOrdonnanceActivity extends AppCompatActivity {

    private EditText etPatientName, etDateOrdonnance, etMedicaments;
    private Button btnSave;
    private FirebaseFirestore db;
    private Ordonnance ordonnance;
    private String ordonnanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_ordonnance);

        // Initialisation des vues
        etPatientName = findViewById(R.id.etPatientName);
        etDateOrdonnance = findViewById(R.id.etDateOrdonnance);
        etMedicaments = findViewById(R.id.etMedicaments);
        btnSave = findViewById(R.id.btnSave);

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Récupérer l'ID de l'ordonnance passée via Intent
        ordonnanceId = getIntent().getStringExtra("ordonnanceId");

        // Charger les informations de l'ordonnance
        fetchOrdonnanceDetails();

        // Gérer la sauvegarde des modifications
        btnSave.setOnClickListener(v -> updateOrdonnance());
    }

    private void fetchOrdonnanceDetails() {
        DocumentReference docRef = db.collection("ordonnances").document(ordonnanceId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ordonnance = task.getResult().toObject(Ordonnance.class);
                if (ordonnance != null) {
                    // Remplir les champs avec les données existantes
                    etPatientName.setText(ordonnance.getPatientName());
                    etDateOrdonnance.setText(ordonnance.getDate());
                    etMedicaments.setText(ordonnance.getMedicaments());
                }
            } else {
                Toast.makeText(ModifierOrdonnanceActivity.this, "Erreur de récupération de l'ordonnance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrdonnance() {
        // Récupérer les valeurs des champs modifiés
        String updatedPatientName = etPatientName.getText().toString();
        String updatedDate = etDateOrdonnance.getText().toString();
        String updatedMedicaments = etMedicaments.getText().toString();

        // Vérifier si les champs sont remplis
        if (updatedPatientName.isEmpty() || updatedDate.isEmpty() || updatedMedicaments.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mettre à jour l'ordonnance dans Firestore
        ordonnance.setPatientName(updatedPatientName);
        ordonnance.setDate(updatedDate);
        ordonnance.setMedicaments(updatedMedicaments);

        db.collection("ordonnances").document(ordonnanceId)
                .set(ordonnance)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ModifierOrdonnanceActivity.this, "Ordonnance mise à jour", Toast.LENGTH_SHORT).show();

                    // Retour à l'activité précédente avec un résultat
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedOrdonnanceId", ordonnanceId);
                    setResult(RESULT_OK, resultIntent);

                    finish();  // Fermer l'activité et revenir à la liste
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ModifierOrdonnanceActivity.this, "Erreur de mise à jour", Toast.LENGTH_SHORT).show();
                });
    }
}

