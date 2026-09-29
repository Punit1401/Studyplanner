package com.example.studyplanner;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView txtSelectedDate;
    RecyclerView recyclerTasks, recyclerExams;
    List<TaskModel> taskList;
    List<ExamModel> examList;
    TaskCalendarAdapter taskAdapter;
    ExamAdapter examAdapter;
    FirebaseFirestore db;
    String userId;
    String selectedDate;
    ListenerRegistration taskListener, examListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        recyclerExams = findViewById(R.id.recyclerExams);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        taskList = new ArrayList<>();
        examList = new ArrayList<>();
        taskAdapter = new TaskCalendarAdapter(taskList);
        examAdapter = new ExamAdapter(examList);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(taskAdapter);
        recyclerExams.setLayoutManager(new LinearLayoutManager(this));
        recyclerExams.setAdapter(examAdapter);
        recyclerTasks.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        txtSelectedDate.setText("📍 " + selectedDate);
        loadTasks(selectedDate);
        loadExams(selectedDate);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            txtSelectedDate.setAlpha(0f);
            txtSelectedDate.setText("📍 " + selectedDate);
            txtSelectedDate.animate().alpha(1f).setDuration(300);
            loadTasks(selectedDate);
            loadExams(selectedDate);
        });
        setupBottomNav();
    }
    private void loadTasks(String date) {
        if (taskListener != null) {
            taskListener.remove();
        }
        taskListener = db.collection("users")
                .document(userId)
                .collection("tasks")
                .whereEqualTo("date", date)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    taskList.clear();
                    for (DocumentSnapshot doc : value) {
                        TaskModel task = doc.toObject(TaskModel.class);
                        taskList.add(task);
                    }
                    taskAdapter.notifyDataSetChanged();
                    recyclerTasks.scheduleLayoutAnimation();
                    if(taskList.isEmpty()){
                        txtSelectedDate.setText("📍 " + date + " - No tasks 🎉");
                    } else {
                        txtSelectedDate.setText("📍 " + date);
                    }
                });
    }
    private void loadExams(String date) {
        if (examListener != null) {
            examListener.remove();
        }
        examListener = db.collection("users")
                .document(userId)
                .collection("exams")
                .whereEqualTo("date", date)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    examList.clear();
                    for (DocumentSnapshot doc : value) {
                        ExamModel exam = doc.toObject(ExamModel.class);
                        examList.add(exam);
                    }
                    examAdapter.notifyDataSetChanged();
                    recyclerExams.scheduleLayoutAnimation();
                });
    }
    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_calendar);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_calendar) {
                return true;
            }
            else if (item.getItemId() == R.id.nav_focus) {
                startActivity(new Intent(this, FocusActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
        if (taskListener != null) taskListener.remove();
        if (examListener != null) examListener.remove();
    }
}