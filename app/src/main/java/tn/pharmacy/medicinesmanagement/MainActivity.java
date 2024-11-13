package tn.pharmacy.medicinesmanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import tn.pharmacy.medicinesmanagement.entities.Medicine;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE_PERMISSION = 112;
    private static final int REQUEST_MEDIA_PERMISSION = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for appropriate permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        REQUEST_MEDIA_PERMISSION);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10+ (API 29 to API 32)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE_PERMISSION);
            }
        }

        // Button to add a new medicine
        Button btnAddMedicine = findViewById(R.id.btnAddMedicine);
        btnAddMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMedecine.class);
            startActivity(intent);
        });

        // Button to show all medicines
        Button btnShowMedicines = findViewById(R.id.btnShowMedicines);
        btnShowMedicines.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowMedicines.class);
            startActivity(intent);
        });

        // Button to update a medicine
        Button btnUpdateMedicine = findViewById(R.id.btnUpdateMedicine);
        btnUpdateMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UpdateMedicine.class);
            startActivity(intent);
        });

        // Button to delete a medicine
        Button btnDeleteMedicine = findViewById(R.id.btnDeleteMedicine);
        btnDeleteMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowMedicines.class);
            startActivity(intent);
        });

        // Button to export medicines to Excel
        Button btnExportMedicines = findViewById(R.id.btnExportMedicines);
        btnExportMedicines.setOnClickListener(v -> {
            List<Medicine> medicines = getMedicinesList(); // Fetch the medicines list
            exportMedicinesToExcel.exportMedicinesToExcel(MainActivity.this, medicines); // Export to Excel
        });
    }

    // Example method to fetch medicines (replace with your actual data fetching method)
    private List<Medicine> getMedicinesList() {
        // Replace with actual logic to fetch medicines
        return new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION || requestCode == REQUEST_MEDIA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. You can now export the data.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied. Cannot export data.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
