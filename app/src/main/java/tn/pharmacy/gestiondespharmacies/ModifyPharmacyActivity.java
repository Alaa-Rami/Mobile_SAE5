package tn.pharmacy.gestiondespharmacies;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import tn.pharmacy.gestiondespharmacies.entities.Pharmacie;

public class ModifyPharmacyActivity extends AppCompatActivity {

    private static final String TAG = "ModifyPharmacyActivity"; // Définir TAG pour les logs

    private EditText editTextNom, editTextAdresse, editTextVille, editTextTelephone, editTextHoraires;
    private Switch switchEstDeGarde;
    private Button saveButton;
    private String pharmacyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pharmacy);

        // Récupérer l'ID de la pharmacie à modifier
        pharmacyId = getIntent().getStringExtra("pharmacyId");
        Log.d(TAG, "Pharmacy ID: " + pharmacyId); // Vérifier que l'ID est bien récupéré

        // Vérification de l'ID
        if (pharmacyId == null || pharmacyId.isEmpty()) {
            Log.e(TAG, "ID de la pharmacie est vide ou invalide !");
            Toast.makeText(ModifyPharmacyActivity.this, "ID de la pharmacie invalide", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si l'ID est invalide
            return;
        }

        // Initialiser les champs
        editTextNom = findViewById(R.id.editTextNom);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextVille = findViewById(R.id.editTextVille);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        editTextHoraires = findViewById(R.id.editTextHoraires);
        switchEstDeGarde = findViewById(R.id.switchEstDeGarde);  // Le switch pour "Est de garde"
        saveButton = findViewById(R.id.saveButton);

        // Charger les informations de la pharmacie dans les champs
        loadPharmacyData();

        // Écouter le clic sur le bouton de sauvegarde
        saveButton.setOnClickListener(v -> {
            saveUpdatedPharmacy();
        });
    }

    private void loadPharmacyData() {
        // Récupérer les données de la pharmacie depuis Firestore en utilisant l'ID
        FirebaseFirestore.getInstance().collection("pharmacies")
                .document(pharmacyId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Pharmacie pharmacy = documentSnapshot.toObject(Pharmacie.class);
                        if (pharmacy != null) {
                            // Remplir les champs avec les données
                            editTextNom.setText(pharmacy.getNom());
                            editTextAdresse.setText(pharmacy.getAdresse());
                            editTextVille.setText(pharmacy.getVille());
                            editTextTelephone.setText(pharmacy.getTelephone());
                            editTextHoraires.setText(pharmacy.getHoraires());
                            switchEstDeGarde.setChecked(pharmacy.isEstDeGarde());  // Remplir le switch
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Log l'erreur pour un diagnostic plus détaillé
                    Log.e(TAG, "Erreur lors du chargement des données: ", e);
                    Toast.makeText(ModifyPharmacyActivity.this, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUpdatedPharmacy() {
        // Récupérer les nouvelles valeurs
        String nom = editTextNom.getText().toString().trim();
        String adresse = editTextAdresse.getText().toString().trim();
        String ville = editTextVille.getText().toString().trim();
        String telephone = editTextTelephone.getText().toString().trim();
        String horaires = editTextHoraires.getText().toString().trim();
        boolean estDeGarde = switchEstDeGarde.isChecked();  // Récupérer l'état du switch

        // Vérifier que tous les champs sont remplis
        if (nom.isEmpty() || adresse.isEmpty() || ville.isEmpty() || telephone.isEmpty() || horaires.isEmpty()) {
            Toast.makeText(ModifyPharmacyActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;  // Retourner si un champ est vide
        }

        // Log les données avant la mise à jour
        Log.d(TAG, "Données à mettre à jour: Nom = " + nom + ", Adresse = " + adresse + ", Ville = " + ville + ", Téléphone = " + telephone + ", Horaires = " + horaires + ", Est de garde = " + estDeGarde);

        // Mettre à jour les informations de la pharmacie dans Firestore
        FirebaseFirestore.getInstance().collection("pharmacies")
                .document(pharmacyId)
                .update(
                        "nom", nom,
                        "adresse", adresse,
                        "ville", ville,
                        "telephone", telephone,
                        "horaires", horaires,
                        "estDeGarde", estDeGarde
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ModifyPharmacyActivity.this, "Pharmacie mise à jour!", Toast.LENGTH_SHORT).show();
                    finish(); // Retourner à l'activité précédente
                })
                .addOnFailureListener(e -> {
                    // Log l'erreur détaillée pour mieux comprendre pourquoi la mise à jour échoue
                    Log.e(TAG, "Erreur lors de la mise à jour: ", e);
                    Toast.makeText(ModifyPharmacyActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                });
    }
}
