package tn.pharmacy.gestionordonnances2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tn.pharmacy.gestionordonnances2.entities.Ordonnance;

public class OrdonnanceListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MODIFY = 1;
    private RecyclerView recyclerView;
    private OrdonnanceAdapter ordonnanceAdapter;
    private List<Ordonnance> ordonnanceList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordonnance_list);

        // Initialisation des éléments
        recyclerView = findViewById(R.id.recyclerViewOrdonnances);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordonnanceList = new ArrayList<>();
        ordonnanceAdapter = new OrdonnanceAdapter(this, ordonnanceList);
        recyclerView.setAdapter(ordonnanceAdapter);

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Chargement des données depuis Firestore
        fetchOrdonnancesFromFirestore();
    }

    private void fetchOrdonnancesFromFirestore() {
        db.collection("ordonnances")
                .get()  // Récupérer toutes les ordonnances de la collection
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ordonnanceList.clear();  // On vide la liste pour éviter les doublons
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ordonnance ordonnance = document.toObject(Ordonnance.class);
                            ordonnance.setId(document.getId());  // Assignation de l'ID Firestore à l'objet ordonnance
                            ordonnanceList.add(ordonnance);
                        }
                        ordonnanceAdapter.notifyDataSetChanged();  // Notifier l'adaptateur des changements
                    } else {
                        Toast.makeText(OrdonnanceListActivity.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void onItemClick(Ordonnance ordonnance) {
        Intent intent = new Intent(OrdonnanceListActivity.this, ModifierOrdonnanceActivity.class);
        intent.putExtra("ordonnanceId", ordonnance.getId());
        startActivityForResult(intent, REQUEST_CODE_MODIFY);  // Appel de ModifierOrdonnanceActivity
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MODIFY && resultCode == RESULT_OK) {
            String updatedOrdonnanceId = data.getStringExtra("updatedOrdonnanceId");



            fetchOrdonnancesFromFirestore();
        }
    }
}
