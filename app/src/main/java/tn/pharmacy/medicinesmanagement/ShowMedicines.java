package tn.pharmacy.medicinesmanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import tn.pharmacy.medicinesmanagement.entities.Medicine;

public class ShowMedicines extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicines);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view_medicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance("https://medicines-management-firebase-default-rtdb.europe-west1.firebasedatabase.app").getReference("medicines");

        // Initialize medicine list and adapter
        medicineList = new ArrayList<>();
        medicineAdapter = new MedicineAdapter(this, medicineList);

        // Set the adapter to RecyclerView
        recyclerView.setAdapter(medicineAdapter);

        // Retrieve medicines from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list before adding new data
                medicineList.clear();

                // Loop through the snapshot and add medicines to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    medicineList.add(medicine);
                }

                // Notify the adapter that data has changed
                medicineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowMedicines.this, "Failed to load medicines.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
