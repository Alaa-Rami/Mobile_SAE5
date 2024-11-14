package tn.pharmacy.gestiondespharmacies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tn.pharmacy.gestiondespharmacies.entities.Pharmacie;

public class DisplayPharmaciesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PharmacyAdapter adapter;
    private List<Pharmacie> pharmacies;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pharmacies);

        recyclerView = findViewById(R.id.recyclerView);
        firestore = FirebaseFirestore.getInstance();
        pharmacies = new ArrayList<>();

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PharmacyAdapter(pharmacies, this);
        recyclerView.setAdapter(adapter);

        // Charger les pharmacies depuis Firestore
        loadPharmacies();
    }

    private void loadPharmacies() {
        firestore.collection("pharmacies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Pharmacie pharmacy = documentSnapshot.toObject(Pharmacie.class);
                        pharmacies.add(pharmacy);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DisplayPharmaciesActivity.this, "Erreur lors du chargement", Toast.LENGTH_SHORT).show();
                });
    }

    // Gestion du clic sur une pharmacie
    public void onPharmacyClick(View view, Pharmacie pharmacy) {
        Intent intent = new Intent(DisplayPharmaciesActivity.this, ModifyPharmacyActivity.class);
        intent.putExtra("pharmacyId", pharmacy.getId());  // Passer l'ID de la pharmacie
        startActivity(intent);
    }

}
