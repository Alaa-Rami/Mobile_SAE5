package tn.pharmacy.gestiondespharmacies;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchPharmacyActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pharmacy);

        firestore = FirebaseFirestore.getInstance();

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);


        buttonSearch.setOnClickListener(v -> searchPharmacies());
    }

    private void searchPharmacies() {
        String searchQuery = editTextSearch.getText().toString().trim();

        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une ville", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("pharmacies")
                .whereEqualTo("ville", searchQuery)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Afficher les pharmacies trouvées
                        // Vous pouvez utiliser un RecyclerView pour afficher les résultats
                        Log.d("Search", "Pharmacies trouvées : " + queryDocumentSnapshots.size());
                        // Votre logique pour afficher les pharmacies dans un RecyclerView
                    } else {
                        Toast.makeText(this, "Aucune pharmacie trouvée", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur de recherche", Toast.LENGTH_SHORT).show());
    }
}
