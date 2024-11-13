package tn.pharmacy.medicinesmanagement;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import tn.pharmacy.medicinesmanagement.R;
import tn.pharmacy.medicinesmanagement.entities.Medicine;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private Context context;
    private List<Medicine> medicineList;
    private DatabaseReference mDatabase;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        this.mDatabase = FirebaseDatabase.getInstance("https://medicines-management-firebase-default-rtdb.europe-west1.firebasedatabase.app").getReference("medicines");
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        // Bind data to views
        holder.name.setText(medicine.getName());
        holder.description.setText(medicine.getDescription());
        holder.price.setText("Price: $" + medicine.getPrice());
        holder.quantity.setText("Quantity: " + medicine.getQuantity());

        // Update Button Click
        holder.updateButton.setOnClickListener(v -> {
            // Send user to the update screen with the medicine details
            Intent intent = new Intent(context, UpdateMedicine.class);
            intent.putExtra("medicineId", medicine.getId());  // Pass the id
            intent.putExtra("name", medicine.getName());
            intent.putExtra("description", medicine.getDescription());
            intent.putExtra("price", medicine.getPrice());
            intent.putExtra("quantity", medicine.getQuantity());
            intent.putExtra("imageUrl", medicine.getImageUrl());
            context.startActivity(intent);
        });

        // Delete Button Logic (same as before)
        holder.deleteButton.setOnClickListener(v -> {
            mDatabase.child(String.valueOf(medicine.getId())).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            medicineList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Medicine deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete medicine", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price, quantity;
        Button deleteButton, updateButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_name);
            description = itemView.findViewById(R.id.medicine_description);
            price = itemView.findViewById(R.id.medicine_price);
            quantity = itemView.findViewById(R.id.medicine_quantity);
            deleteButton = itemView.findViewById(R.id.btn_delete_medicine);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
