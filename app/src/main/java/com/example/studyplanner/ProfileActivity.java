package com.example.studyplanner;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgProfile;
    TextView txtName, txtEmail, txtEmpty;
    RecyclerView recyclerHistory;
    Button btnLogout;
    HistoryAdapter adapter;
    List<HistoryModel> list;
    FirebaseFirestore db;
    String userId;

    @Override
    protected void onStart() {
        super.onStart();
        loadUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtEmpty = findViewById(R.id.txtEmpty);
        recyclerHistory = findViewById(R.id.recyclerHistory);
        btnLogout = findViewById(R.id.btnLogout);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new HistoryAdapter(this, list);
        recyclerHistory.setItemAnimator(null);
        recyclerHistory.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        loadHistory();
        btnLogout.setOnClickListener(v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
        setupBottomNav();
    }
    private void loadUser(){
        db.collection("users").document(userId)
                .addSnapshotListener((doc, e) -> {
                    if(doc != null && doc.exists()){
                        txtName.setText(doc.getString("name"));
                        txtEmail.setText(doc.getString("email"));
                        imgProfile.setImageResource(R.drawable.logo);
                    }
                });
    }
    private void loadHistory(){
        db.collection("users")
                .document(userId)
                .collection("tasks")
                .addSnapshotListener((query, error) -> {
                    if (query == null) return;
                    Map<String, HistoryModel> map = new HashMap<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String date = doc.getString("date");
                        String subject = doc.getString("subject");
                        Boolean completed = doc.getBoolean("completed");
                        if (date == null || completed == null) continue;
                        if (!map.containsKey(date)) {
                            map.put(date, new HistoryModel(date));
                        }
                        HistoryModel m = map.get(date);
                        m.subjects.add(subject);
                        if (completed) {
                            m.completedCount++;
                        } else {
                            m.pendingCount++;
                        }
                    }
                    List<HistoryModel> result = new ArrayList<>(map.values());
                    Collections.sort(result, (a, b) -> b.date.compareTo(a.date));
                    if (result.size() > 4) {
                        result = result.subList(0, 4);
                    }
                    if (result.isEmpty()) {
                        txtEmpty.setVisibility(View.VISIBLE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                    }
                    adapter.setList(result);
                });
    }
    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_profile);
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
                startActivity(new Intent(this, FocusActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}