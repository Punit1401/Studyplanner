package com.example.studyplanner;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    TextView txtGreeting, txtName, txtProgress, txtCompletion;
    ImageView imgProfileDashboard;
    RecyclerView recyclerTasks, recyclerExams;
    List<TaskModel> taskList;
    TaskAdapter adapter;
    ProgressBar progressBar;
    List<ExamModel> examList;
    ExamAdapter examAdapter;
    FloatingActionButton fab;
    FirebaseFirestore db;
    String userId;
    ListenerRegistration taskListener,examListener,progressListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            }
            else if (item.getItemId() == R.id.nav_calendar) {
                startActivity(new Intent(this, CalendarActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
        txtGreeting = findViewById(R.id.txtGreeting);
        txtName = findViewById(R.id.txtName);
        txtProgress = findViewById(R.id.txtProgress);
        progressBar = findViewById(R.id.progressBar);
        txtCompletion = findViewById(R.id.txtCompletion);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        fab = findViewById(R.id.fabAdd);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imgProfileDashboard = findViewById(R.id.imgProfileDashboard);
        imgProfileDashboard.setImageResource(R.drawable.logo);
        if(hour < 12){
            txtGreeting.setText("Good Morning");
        } else if(hour < 17){
            txtGreeting.setText("Good Afternoon");
        } else {
            txtGreeting.setText("Good Evening");
        }
        txtName.setText("User");
        loadUserData();
        recyclerTasks = findViewById(R.id.recyclerTasks);
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);
        recyclerTasks.setItemAnimator(new DefaultItemAnimator());
        recyclerTasks.setNestedScrollingEnabled(false);
        recyclerTasks.getItemAnimator().setAddDuration(300);
        recyclerTasks.getItemAnimator().setMoveDuration(300);
        recyclerExams = findViewById(R.id.recyclerExams);
        examList = new ArrayList<>();
        examAdapter = new ExamAdapter(examList);
        recyclerExams.setLayoutManager(new LinearLayoutManager(this));
        recyclerExams.setAdapter(examAdapter);
        recyclerExams.setItemAnimator(new DefaultItemAnimator());
        recyclerExams.setNestedScrollingEnabled(false);
        fab.setOnClickListener(v -> {
            v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_bounce));
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.bottom_sheet_add, null);
            dialog.setContentView(view);
            view.findViewById(R.id.btnTask).setOnClickListener(v1 -> {
                startActivity(new Intent(this, AddTaskActivity.class));
                dialog.dismiss();
            });
            view.findViewById(R.id.btnExam).setOnClickListener(v1 -> {
                startActivity(new Intent(this, AddExamActivity.class));
                dialog.dismiss();
            });
            dialog.show();
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadTasksRealtime();
        loadExamsRealtime();
        loadProgressRealtime();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (taskListener != null) taskListener.remove();
        if (examListener != null) examListener.remove();
        if (progressListener != null) progressListener.remove();
    }
    private void loadUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .addSnapshotListener((document, error) -> {
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        if (name != null && !name.isEmpty()) {
                            txtName.setText(name);
                        }
                    }
                });
    }
    private void loadTasksRealtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(new Date());
        taskListener = db.collection("users")
                .document(userId)
                .collection("tasks")
                .whereEqualTo("date", todayDate)
                .whereEqualTo("completed", false) // 🔥 FILTER HERE
                .addSnapshotListener((value, error) -> {
                    if (value == null) return;
                    taskList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        TaskModel task = doc.toObject(TaskModel.class);
                        task.setId(doc.getId());
                        taskList.add(task);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerTasks.scheduleLayoutAnimation();
                });
    }
    private void loadProgressRealtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(new Date());
        progressListener = db.collection("users")
                .document(userId)
                .collection("tasks")
                .whereEqualTo("date", todayDate)
                .addSnapshotListener((value, error) -> {
                    if (value == null) return;
                    int total = value.size();
                    int completed = 0;
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Boolean done = doc.getBoolean("completed");
                        if (done != null && done) {
                            completed++;
                        }
                    }
                    if (total == 0) {
                        txtProgress.setText("No tasks today");
                        progressBar.setProgress(0);
                        txtCompletion.setText("0%");
                        return;
                    }
                    int progress = (completed * 100) / total;
                    txtProgress.setText(completed + " / " + total + " tasks");
                    progressBar.setProgress(progress);
                    txtCompletion.setText(progress + "%");
                });
    }
    private void loadExamsRealtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(new Date());
        examListener = db.collection("users")
                .document(userId)
                .collection("exams")
                .whereGreaterThanOrEqualTo("date", todayDate)
                .addSnapshotListener((value, error) -> {
                    if (value == null) return;
                    examList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        ExamModel exam = doc.toObject(ExamModel.class);
                        examList.add(exam);
                    }
                    examAdapter.notifyDataSetChanged();
                });
    }
}