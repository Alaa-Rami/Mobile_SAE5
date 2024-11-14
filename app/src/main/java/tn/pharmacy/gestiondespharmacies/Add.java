package tn.pharmacy.gestiondespharmacies;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import tn.pharmacy.gestiondespharmacies.entities.Pharmacie;

public class Add extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText editTextId, editTextNom, editTextAdresse, editTextTelephone, editTextHoraires;
    private Spinner spinnerVille;
    private Button buttonAddPharmacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firestore = FirebaseFirestore.getInstance();

        editTextId = findViewById(R.id.editTextId);
        editTextNom = findViewById(R.id.editTextNom);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        spinnerVille = findViewById(R.id.spinnerVille);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        editTextHoraires = findViewById(R.id.editTextHoraires);
        buttonAddPharmacy = findViewById(R.id.buttonAddPharmacy);

        // Configurer le Spinner avec une liste de villes
        List<String> villes = Arrays.asList("Tunis", "Sfax", "Sousse", "Gabès", "Bizerte");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, villes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVille.setAdapter(adapter);

        buttonAddPharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPharmacy();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addPharmacy() {
        String id = editTextId.getText().toString();
        String nom = editTextNom.getText().toString();
        String adresse = editTextAdresse.getText().toString();
        String ville = spinnerVille.getSelectedItem().toString(); // Obtenir la ville sélectionnée
        String telephone = editTextTelephone.getText().toString();
        String horaires = editTextHoraires.getText().toString();

        if (id.isEmpty() || nom.isEmpty() || adresse.isEmpty() || ville.isEmpty() || telephone.isEmpty() || horaires.isEmpty()) {
            Toast.makeText(Add.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        Pharmacie pharmacie = new Pharmacie(id, nom, adresse, ville, telephone, horaires, false);

        firestore.collection("pharmacies")
                .add(pharmacie)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Add.this, "Pharmacie ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(Add.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show());
    }
}
