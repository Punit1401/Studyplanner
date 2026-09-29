package com.example.studyplanner;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    List<TaskModel> list;
    public TaskAdapter(List<TaskModel> list){
        this.list = list;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TaskModel task = list.get(position);
        holder.taskName.setText(task.getName());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isCompleted());
        holder.itemView.setOnClickListener(v -> {
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction(() ->
                    v.animate().scaleX(1f).scaleY(1f).setDuration(80)
            );
        });
        if(task.isCompleted()){
            holder.taskName.setTextColor(Color.GREEN);
            holder.taskName.setPaintFlags(
                    holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.taskName.setTextColor(Color.parseColor("#FFA500"));
            holder.taskName.setPaintFlags(0);
        }
        holder.checkBox.setOnClickListener(v -> {
            holder.checkBox.setChecked(false);
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                showConfirmDialog(holder.itemView, task, currentPosition);
            }
        });
    }
    private void showConfirmDialog(View view, TaskModel task, int position) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Complete Task 🎉")
                .setMessage("Mark this task as completed?")
                .setIcon(android.R.drawable.checkbox_on_background)
                .setPositiveButton("Yes", (dialog, which) -> {
                    markTaskCompleted(view, task, position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
    private void markTaskCompleted(View view, TaskModel task, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.getId())
                .update("completed", true)
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        CheckBox checkBox;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            checkBox = itemView.findViewById(R.id.checkTask);
        }
    }
}