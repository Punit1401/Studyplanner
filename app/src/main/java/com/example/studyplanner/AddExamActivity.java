package com.example.studyplanner;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class AddExamActivity extends AppCompatActivity {
    EditText examName, examSubject, examDate;
    RadioGroup priorityGroup;
    Button btnAdd;
    FirebaseFirestore db;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        ImageView btnBack = findViewById(R.id.btnBack);
        examName = findViewById(R.id.examName);
        examSubject = findViewById(R.id.examSubject);
        examDate = findViewById(R.id.examDate);
        priorityGroup = findViewById(R.id.priorityGroup);
        btnAdd = findViewById(R.id.btnAddExam);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnBack.setOnClickListener(v -> {
            finish();
        });
        examDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view, year, month, day) -> {
                        String date = String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", year, month + 1, day);
                        examDate.setText(date);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
            dialog.show();
        });
        btnAdd.setOnClickListener(v -> {
            int selectedId = priorityGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Select Priority", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selected = findViewById(selectedId);
            String name = examName.getText().toString();
            String subject = examSubject.getText().toString();
            String date = examDate.getText().toString();
            String priority = selected.getText().toString();
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MyDialog)
                    .setTitle("Confirm Exam")
                    .setMessage("Add this exam?\n\n" +
                            "📘 " + name + "\n" +
                            "📅 " + date + "\n" +
                            "⚡ " + priority)
                    .setPositiveButton("Add", (dialog, which) -> {
                        Map<String, Object> exam = new HashMap<>();
                        exam.put("name", name);
                        exam.put("subject", subject);
                        exam.put("date", date);
                        exam.put("Priority", priority);
                        db.collection("users")
                                .document(userId)
                                .collection("exams")
                                .add(exam)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Exam Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}