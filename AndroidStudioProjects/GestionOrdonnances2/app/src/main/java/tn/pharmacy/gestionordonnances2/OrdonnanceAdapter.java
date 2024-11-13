package tn.pharmacy.gestionordonnances2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import java.util.List;

import tn.pharmacy.gestionordonnances2.entities.Ordonnance;

public class OrdonnanceAdapter extends RecyclerView.Adapter<OrdonnanceAdapter.OrdonnanceViewHolder> {

    private List<Ordonnance> ordonnanceList;
    private Context context;
    private FirebaseFirestore firestore;

    public OrdonnanceAdapter(Context context, List<Ordonnance> ordonnanceList) {
        this.context = context;
        this.ordonnanceList = ordonnanceList;
        this.firestore = FirebaseFirestore.getInstance();  // Utilisation de Firestore
    }

    @NonNull
    @Override
    public OrdonnanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ordonnance, parent, false);
        return new OrdonnanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdonnanceViewHolder holder, int position) {
        Ordonnance ordonnance = ordonnanceList.get(position);

        // Affichage des données dans l'élément de la liste
        holder.tvPatientName.setText("Nom du patient : " + ordonnance.getPatientName());
        holder.tvDateOrdonnance.setText("Date : " + ordonnance.getDate());
        holder.tvMedicaments.setText("Médicaments : " + ordonnance.getMedicaments());

        // Suppression d'une ordonnance avec Firestore
        holder.btnSupprimer.setOnClickListener(v -> {
            String id = ordonnance.getId();
            if (id != null) {
                DocumentReference ordonnanceRef = firestore.collection("ordonnances").document(id);
                ordonnanceRef.delete()
                        .addOnSuccessListener(aVoid -> {
                            ordonnanceList.remove(position);
                            notifyItemRemoved(position);
                        })
                        .addOnFailureListener(e -> {
                            // Gérer l'échec de la suppression
                        });
            }
        });

        // Modification d'une ordonnance (à implémenter selon vos besoins)
        holder.btnModifier.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifierOrdonnanceActivity.class);
            intent.putExtra("ordonnanceId", ordonnance.getId());
            context.startActivity(intent);
        });

        // Archivage d'une ordonnance
        holder.btnArchive.setOnClickListener(v -> {
            archiveOrdonnance(position);
        });
    }

    @Override
    public int getItemCount() {
        return ordonnanceList.size();
    }

    // ViewHolder pour les éléments de la liste
    public static class OrdonnanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvDateOrdonnance, tvMedicaments;
        Button btnModifier, btnSupprimer, btnArchive;

        public OrdonnanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvDateOrdonnance = itemView.findViewById(R.id.tvDateOrdonnance);
            tvMedicaments = itemView.findViewById(R.id.tvMedicaments);
            btnModifier = itemView.findViewById(R.id.btnModifier);
            btnSupprimer = itemView.findViewById(R.id.btnSupprimer);
            btnArchive = itemView.findViewById(R.id.btnArchive); // Ajouter ici le bouton d'archivage
        }
    }

    // Méthode pour archiver l'ordonnance
    private void archiveOrdonnance(int position) {
        Ordonnance ordonnance = ordonnanceList.get(position);
        String ordonnanceId = ordonnance.getId();

        // Déplacer l'ordonnance vers la collection "archived_ordonnances"
        firestore.collection("archived_ordonnances").document(ordonnanceId)
                .set(ordonnance)
                .addOnSuccessListener(aVoid -> {
                    // Supprimer l'ordonnance de la collection "ordonnances"
                    firestore.collection("ordonnances").document(ordonnanceId)
                            .delete()
                            .addOnSuccessListener(aVoid1 -> {
                                // Retirer l'élément de la liste et notifier l'adaptateur
                                ordonnanceList.remove(position);
                                notifyItemRemoved(position);
                                // Afficher un message de confirmation
                                Toast.makeText(context, "Ordonnance archivée", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur d'archivage", Toast.LENGTH_SHORT).show();
                });
    }
}

