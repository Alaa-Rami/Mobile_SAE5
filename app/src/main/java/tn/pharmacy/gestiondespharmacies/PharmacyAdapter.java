package tn.pharmacy.gestiondespharmacies;

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

import java.util.List;

import tn.pharmacy.gestiondespharmacies.entities.Pharmacie;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private List<Pharmacie> pharmacies;
    private FirebaseFirestore firestore;
    private Context context;

    public PharmacyAdapter(List<Pharmacie> pharmacies, Context context) {
        this.pharmacies = pharmacies;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gonfler la vue de l'élément de la pharmacie
        View view = LayoutInflater.from(context).inflate(R.layout.pharmacy_item, parent, false);
        return new PharmacyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
        Pharmacie pharmacy = pharmacies.get(position);

        // Afficher les informations de la pharmacie dans le TextView
        String pharmacieInfo = "Nom: " + pharmacy.getNom() + "\n" +
                "Adresse: " + pharmacy.getAdresse() + "\n" +
                "Ville: " + pharmacy.getVille() + "\n" +
                "Téléphone: " + pharmacy.getTelephone() + "\n" +
                "Horaires: " + pharmacy.getHoraires() + "\n" +
                "Est de garde: " + (pharmacy.isEstDeGarde() ? "Oui" : "Non");

        holder.pharmacyInfo.setText(pharmacieInfo);

        // Gérer le clic pour modifier la pharmacie
        holder.modifyButton.setOnClickListener(v -> {
            // Intention de modifier la pharmacie
            Intent intent = new Intent(context, ModifyPharmacyActivity.class);
            intent.putExtra("pharmacyId", pharmacy.getId());  // Passer l'ID pour la modification
            context.startActivity(intent);
        });

        // Gérer le clic pour supprimer la pharmacie
        holder.deleteButton.setOnClickListener(v -> deletePharmacy(pharmacy.getId(), position));
    }

    @Override
    public int getItemCount() {
        return pharmacies.size();
    }

    private void deletePharmacy(String pharmacyId, int position) {
        firestore.collection("pharmacies")
                .document(pharmacyId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    pharmacies.remove(position); // Supprimer la pharmacie de la liste
                    notifyItemRemoved(position); // Mettre à jour la vue
                    Toast.makeText(context, "Pharmacie supprimée!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                });
    }

    public static class PharmacyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyInfo;  // TextView pour afficher les infos de la pharmacie
        Button modifyButton, deleteButton;  // Boutons pour modifier et supprimer

        public PharmacyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialiser les vues
            pharmacyInfo = itemView.findViewById(R.id.pharmacyInfo);
            modifyButton = itemView.findViewById(R.id.modifyButton); // Assurez-vous que l'ID est correct
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
