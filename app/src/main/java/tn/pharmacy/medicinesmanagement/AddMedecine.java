package tn.pharmacy.medicinesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tn.pharmacy.medicinesmanagement.entities.Medicine;

public class AddMedecine extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medecine);
// Initialize back button
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> {
            // Finish the current activity and return to the previous one
            finish();
        });

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance("https://medicines-management-firebase-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        // Handle window insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        TextInputEditText medecineNameEditText = findViewById(R.id.medecine_name);
        TextInputEditText medecineDescriptionEditText = findViewById(R.id.medecine_description);
        TextInputEditText medecinePriceEditText = findViewById(R.id.medecine_price);
        TextInputEditText medecineQuantityEditText = findViewById(R.id.medecine_quantity);
        TextInputEditText medecineImageUrlEditText = findViewById(R.id.medecine_image_url);

        // Initialize add button
        Button addMedicineButton = findViewById(R.id.add_medecine);

        addMedicineButton.setOnClickListener(view -> {
            // Retrieve values from input fields
            String name = medecineNameEditText.getText().toString().trim();
            String description = medecineDescriptionEditText.getText().toString().trim();
            String priceStr = medecinePriceEditText.getText().toString().trim();
            String quantityStr = medecineQuantityEditText.getText().toString().trim();
            String imageUrl = medecineImageUrlEditText.getText().toString().trim();

            // Validate the inputs
            if (TextUtils.isEmpty(name)) {
                medecineNameEditText.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(description)) {
                medecineDescriptionEditText.setError("Description is required");
                return;
            }
            if (TextUtils.isEmpty(priceStr)) {
                medecinePriceEditText.setError("Price is required");
                return;
            }
            if (TextUtils.isEmpty(quantityStr)) {
                medecineQuantityEditText.setError("Quantity is required");
                return;
            }
            if (TextUtils.isEmpty(imageUrl)) {
                medecineImageUrlEditText.setError("Image URL is required");
                return;
            }

            // Convert price and quantity to correct types
            float price;
            int quantity;
            try {
                price = Float.parseFloat(priceStr);
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Medicine object from user input
            Medicine medicine = new Medicine(0L, name, "Painkiller", description, price, quantity, imageUrl);  // Set the type to "Painkiller" as per your structure or remove it if dynamic

            // Push the medicine data to Firebase
            String medicineId = mDatabase.child("medicines").push().getKey();
            if (medicineId != null) {
                mDatabase.child("medicines").child(medicineId).setValue(medicine).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMedecine.this, ShowMedicines.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to add medicine", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Optionally, clear the form
            medecineNameEditText.setText("");
            medecineDescriptionEditText.setText("");
            medecinePriceEditText.setText("");
            medecineQuantityEditText.setText("");
            medecineImageUrlEditText.setText("");
        });

    }


}
