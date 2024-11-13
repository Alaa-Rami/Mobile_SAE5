package tn.pharmacy.medicinesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UpdateMedicine extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextInputEditText nameEditText, descriptionEditText, priceEditText, quantityEditText, imageUrlEditText;
    private Button updateButton;
    private Long medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance("https://medicines-management-firebase-default-rtdb.europe-west1.firebasedatabase.app").getReference("medicines");

        // Initialize views
        nameEditText = findViewById(R.id.medecine_name);
        descriptionEditText = findViewById(R.id.medecine_description);
        priceEditText = findViewById(R.id.medecine_price);
        quantityEditText = findViewById(R.id.medecine_quantity);
        imageUrlEditText = findViewById(R.id.medecine_image_url);
        updateButton = findViewById(R.id.update_medecine_button);

        // Get the medicine details from the Intent
        Intent intent = getIntent();
        medicineId = intent.getLongExtra("medicineId", -1);
        nameEditText.setText(intent.getStringExtra("name"));
        descriptionEditText.setText(intent.getStringExtra("description"));
        priceEditText.setText(String.valueOf(intent.getFloatExtra("price", 0)));
        quantityEditText.setText(String.valueOf(intent.getIntExtra("quantity", 0)));
        imageUrlEditText.setText(intent.getStringExtra("imageUrl"));

        // Handle the update button click
        updateButton.setOnClickListener(view -> {
            // Retrieve the updated values
            String name = nameEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();
            String quantityStr = quantityEditText.getText().toString().trim();
            String imageUrl = imageUrlEditText.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(quantityStr) || TextUtils.isEmpty(imageUrl)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            // Create updated Medicine object with the updated values
            Medicine updatedMedicine = new Medicine(medicineId, name, "Painkiller", description, price, quantity, imageUrl);

            // Update the medicine in Firebase based on the unique `medicineId`
            mDatabase.child(String.valueOf(medicineId)).setValue(updatedMedicine)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Medicine updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the update activity and return to the previous screen
                        } else {
                            Toast.makeText(this, "Failed to update medicine", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}