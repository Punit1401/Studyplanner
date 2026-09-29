package com.example.studyplanner;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<HistoryModel> list;
    public HistoryAdapter(Context context, List<HistoryModel> list) {
        this.context = context;
        this.list = list;
    }
    public void setList(List<HistoryModel> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        HistoryModel m = list.get(i);
        h.txtDate.setText(m.date);
        h.txtSubjects.setText("Subjects: " + TextUtils.join(", ", m.subjects));
        h.txtCompleted.setText("Completed: " + m.completedCount);
        h.txtPending.setText("Pending: " + m.pendingCount);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtSubjects, txtCompleted, txtPending;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtSubjects = itemView.findViewById(R.id.txtSubjects);
            txtCompleted = itemView.findViewById(R.id.txtCompleted);
            txtPending = itemView.findViewById(R.id.txtPending);
        }
    }
}