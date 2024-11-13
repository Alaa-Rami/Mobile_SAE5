package tn.pharmacy.gestionordonnances2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Bouton pour ajouter une ordonnance
        Button buttonAjouterOrdonnance = findViewById(R.id.buttonAjouterOrdonnance);
        buttonAjouterOrdonnance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AjouterOrdonnanceActivity.class);
                startActivity(intent);
            }
        });

        // Nouveau bouton pour afficher les ordonnances
        Button buttonShowOrdonnances = findViewById(R.id.btnShowOrdonnances);
        buttonShowOrdonnances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdonnanceListActivity.class);
                startActivity(intent); // Lancer l'activité pour afficher la liste des ordonnances
            }
        });

        // Gestion de Edge-to-Edge UI (facultatif)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
