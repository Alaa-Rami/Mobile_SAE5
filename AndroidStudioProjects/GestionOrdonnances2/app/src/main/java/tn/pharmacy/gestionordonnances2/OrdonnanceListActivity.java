package tn.pharmacy.gestionordonnances2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

    private RecyclerView recyclerView;
    private OrdonnanceAdapter ordonnanceAdapter;
    private List<Ordonnance> ordonnanceList;
    private List<Ordonnance> filteredList;
    private FirebaseFirestore db;
    private EditText searchEditText;
    private Spinner dateFilterSpinner;
    private CheckBox medicamentsFilterCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordonnance_list);

        // Initialisation des éléments
        recyclerView = findViewById(R.id.recyclerViewOrdonnances);
        searchEditText = findViewById(R.id.searchEditText);
        dateFilterSpinner = findViewById(R.id.dateFilterSpinner);
        medicamentsFilterCheckBox = findViewById(R.id.medicamentsFilterCheckBox);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordonnanceList = new ArrayList<>();
        filteredList = new ArrayList<>();
        ordonnanceAdapter = new OrdonnanceAdapter(this, filteredList);
        recyclerView.setAdapter(ordonnanceAdapter);

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Charger les données depuis Firestore
        fetchOrdonnancesFromFirestore();

        // Initialiser le Spinner avec des dates spécifiques (exemple)
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_filter_options, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateFilterSpinner.setAdapter(dateAdapter);

        // Ajouter un écouteur pour la recherche et les filtres
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterList();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Filtrer lors de la sélection dans le Spinner de date
        dateFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filterList();
            }
        });

        // Filtrer lors de l'activation ou désactivation de la case à cocher "Filtrer par médicaments"
        medicamentsFilterCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> filterList());
    }

    private void fetchOrdonnancesFromFirestore() {
        db.collection("ordonnances")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ordonnanceList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ordonnance ordonnance = document.toObject(Ordonnance.class);
                            ordonnance.setId(document.getId());
                            ordonnanceList.add(ordonnance);
                        }
                        filteredList.addAll(ordonnanceList);
                        ordonnanceAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrdonnanceListActivity.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterList() {
        filteredList.clear();
        String query = searchEditText.getText().toString().toLowerCase();
        String selectedDateFilter = (String) dateFilterSpinner.getSelectedItem();
        boolean filterByMedicaments = medicamentsFilterCheckBox.isChecked();

        for (Ordonnance ordonnance : ordonnanceList) {
            boolean matchesQuery = ordonnance.getPatientName().toLowerCase().contains(query) ||
                    ordonnance.getMedicaments().toLowerCase().contains(query) ||
                    ordonnance.getDate().contains(query);
            boolean matchesDate = selectedDateFilter.equals("Toutes") || ordonnance.getDate().equals(selectedDateFilter);
            boolean matchesMedicaments = !filterByMedicaments || ordonnance.getMedicaments().toLowerCase().contains(query);

            if (matchesQuery && matchesDate && matchesMedicaments) {
                filteredList.add(ordonnance);
            }
        }
        ordonnanceAdapter.notifyDataSetChanged();
    }
}

