package com.example.studyplanner;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class TaskCalendarAdapter extends RecyclerView.Adapter<TaskCalendarAdapter.ViewHolder> {
    List<TaskModel> list;
    public TaskCalendarAdapter(List<TaskModel> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = list.get(position);
        holder.taskName.setText(task.getName());
        holder.taskName.setPaintFlags(
                holder.taskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
        );
        holder.taskName.setAlpha(1f);
        if (task.isCompleted()) {
            holder.taskName.setTextColor(Color.parseColor("#4CAF50"));
            holder.taskName.setPaintFlags(
                    holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.taskName.setAlpha(0.6f);
        } else {
            holder.taskName.setTextColor(Color.parseColor("#FFA500"));
        }
        Log.d("TASK_DEBUG", task.getName() + " -> " + task.isCompleted());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
        }
    }
}