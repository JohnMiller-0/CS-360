package com.snhu.cs360_project_miller;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeightEntryAdapter extends RecyclerView.Adapter<WeightEntryAdapter.ViewHolder> {
    private List<WeightEntry> weightEntries;
    private final weightTrackerDBHelper databaseHelper;
    private final Context context;

    public WeightEntryAdapter(Context context, List<WeightEntry> weightEntries) {
        this.context = context;
        this.databaseHelper = weightTrackerDBHelper.getInstance(context);
        this.weightEntries = weightEntries;
    }

    // Method to update the dataset
    public void updateData(List<WeightEntry> newEntries) {
        weightEntries.clear();
        weightEntries.addAll(newEntries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeightEntry entry = weightEntries.get(position);
        holder.textDate.setText(entry.getDate());

        String weight = String.valueOf(entry.getWeight()) + context.getString(R.string.pounds);
        holder.textWeight.setText(weight);

        // Delete button functionality
        holder.buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this weight entry?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        databaseHelper.deleteWeight(entry.getId()); // Delete from DB
                        weightEntries.remove(position); // Remove from list
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, weightEntries.size()); // Refresh list properly
                    })
                    .setNegativeButton("Cancel", null) // Dismiss dialog on cancel
                    .show();
        });

        // Edit button functionality
        holder.buttonEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit Weight Entry");

            // Create an input field for new weight
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setText(String.valueOf(entry.getWeight())); // Pre-fill with existing weight
            builder.setView(input);

            // Set up buttons
            builder.setPositiveButton("Save", (dialog, which) -> {
                String newWeightStr = input.getText().toString().trim();
                if (!newWeightStr.isEmpty()) {
                    int newWeight = Integer.parseInt((newWeightStr));
                    databaseHelper.editWeight(entry.getId(), newWeight); // Update DB

                    // Update UI
                    entry.setWeight(newWeight);
                    notifyItemChanged(position);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });



    }

    @Override
    public int getItemCount() {
        return weightEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textWeight;
        Button buttonDelete, buttonEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDateValue); // Updated ID
            textWeight = itemView.findViewById(R.id.textWeightValue); // Updated ID
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}
