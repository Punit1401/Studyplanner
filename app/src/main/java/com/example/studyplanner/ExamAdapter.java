package com.example.studyplanner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    List<ExamModel> list;
    public ExamAdapter(List<ExamModel> list){
        this.list = list;
    }
    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        ExamModel exam = list.get(position);
        holder.examName.setText(exam.getName());
        holder.examDate.setText(exam.getDate());
        String Priority = exam.getPriority();
        if ("Important".equalsIgnoreCase(Priority)) {
            holder.examContainer.setBackgroundColor(android.graphics.Color.parseColor("#FFEBEE")); // light red
            holder.examName.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
        } else {
            holder.examContainer.setBackgroundColor(android.graphics.Color.parseColor("#FFF3E0")); // light orange
            holder.examName.setTextColor(android.graphics.Color.parseColor("#F57C00"));
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examName, examDate;
        View examContainer;
        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examName = itemView.findViewById(R.id.examName);
            examDate = itemView.findViewById(R.id.examDate);
            examContainer = itemView.findViewById(R.id.examContainer);
        }
    }
}