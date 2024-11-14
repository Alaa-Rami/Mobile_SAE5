package tn.pharmacy.gestiondespharmacies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Button buttonNavigateToAdd, buttonDisplayPharmacies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        // Initialiser les boutons
        buttonNavigateToAdd = findViewById(R.id.buttonNavigateToAdd);
        buttonDisplayPharmacies = findViewById(R.id.buttonDisplayPharmacies);

        // Naviguer vers l'écran d'ajout de pharmacie
        buttonNavigateToAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Add.class);
            startActivity(intent);
        });

        // Naviguer vers l'écran d'affichage des pharmacies
        buttonDisplayPharmacies.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisplayPharmaciesActivity.class);
            startActivity(intent);
        });

        Button buttonSearchPharmacies = findViewById(R.id.buttonSearchPharmacies);

        buttonSearchPharmacies.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchPharmacyActivity.class);
            startActivity(intent);
        });





        // Gérer les fenêtres et les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
