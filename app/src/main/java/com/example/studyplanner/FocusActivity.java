package com.example.studyplanner;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FocusActivity extends AppCompatActivity {
    TextView txtExamName, txtExamDays, txtTasks, txtSuggestion;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String nearestExamName = "No upcoming exams";
    long nearestDays = Long.MAX_VALUE;
    List<String> pendingTasks = new ArrayList<>();
    ListenerRegistration examListener, taskListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        txtExamName = findViewById(R.id.txtExamName);
        txtExamDays = findViewById(R.id.txtExamDays);
        txtTasks = findViewById(R.id.txtTasks);
        txtSuggestion = findViewById(R.id.txtSuggestion);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadExams();
        loadTasks();
        setupBottomNav();
    }
    private void loadExams() {
        if (examListener != null) examListener.remove();
        examListener = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("exams")
                .addSnapshotListener((query, error) -> {
                    if (error != null || query == null) return;
                    nearestDays = Long.MAX_VALUE;
                    nearestExamName = "No upcoming exams";
                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    for (QueryDocumentSnapshot doc : query) {
                        String dateStr = doc.getString("date");
                        String name = doc.getString("name");
                        if (dateStr == null) continue;
                        try {
                            Date examDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr);
                            Date todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(today);
                            long diff = (examDate.getTime() - todayDate.getTime()) / (1000 * 60 * 60 * 24);
                            if (diff >= 0 && diff < nearestDays) {
                                nearestDays = diff;
                                nearestExamName = name;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    updateExamUI();
                    generateSuggestion();
                });
    }
    private void updateExamUI() {
        txtExamName.setText(nearestExamName);
        if (nearestDays == Long.MAX_VALUE) {
            txtExamDays.setText("No upcoming exams");
        } else {
            txtExamDays.setText("In " + nearestDays + " days");
        }
    }
    private void loadTasks() {
        if (taskListener != null) taskListener.remove();
        taskListener = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("tasks")
                .addSnapshotListener((query, error) -> {
                    if (error != null || query == null) return;
                    pendingTasks.clear();
                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    for (QueryDocumentSnapshot doc : query) {
                        String date = doc.getString("date");
                        Boolean completed = doc.getBoolean("completed");
                        String name = doc.getString("name");
                        if (today.equals(date) && (completed == null || !completed)) {
                            pendingTasks.add(name);
                        }
                    }
                    updateTasksUI();
                    generateSuggestion();
                });
    }
    private void updateTasksUI() {
        if (pendingTasks.isEmpty()) {
            txtTasks.setText("No pending tasks today 🎉");
        } else {
            StringBuilder builder = new StringBuilder();
            for (String task : pendingTasks) {
                builder.append("• ").append(task).append("\n");
            }
            txtTasks.setText(builder.toString());
        }
    }
    private void generateSuggestion() {
        String suggestion;
        if (!pendingTasks.isEmpty()) {
            suggestion = "Start: " + pendingTasks.get(0);
        } else if (nearestDays != Long.MAX_VALUE) {
            suggestion = "Prepare for " + nearestExamName;
        } else {
            suggestion = "You're all caught up. Take a break!";
        }
        txtSuggestion.setText(suggestion);
    }
    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_focus);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_focus) {
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (examListener != null) examListener.remove();
        if (taskListener != null) taskListener.remove();
    }
}