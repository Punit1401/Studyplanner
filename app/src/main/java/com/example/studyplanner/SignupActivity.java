package com.example.studyplanner;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText name,email, password;
    Button signupBtn;
    FirebaseAuth auth;
    Dialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signupBtn = findViewById(R.id.btnSignup);
        auth = FirebaseAuth.getInstance();
        loader = new Dialog(this);
        loader.setContentView(R.layout.dialog_loader);
        loader.setCancelable(false);
        signupBtn.setOnClickListener(v -> signupUser());
        TextView signin = findViewById(R.id.txtSignin);
        signin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
    private void signupUser() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        if(userName.isEmpty()){
            name.setError("Enter name");
            return;
        }
        if(userEmail.isEmpty()){
            email.setError("Enter email");
            return;
        }
        if(userPassword.length() < 6){
            password.setError("Password must be 6+ characters");
            return;
        }
        loader.show();
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(task -> {
                    loader.dismiss();
                    if(task.isSuccessful()){
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("name", userName);
                        userMap.put("email", userEmail);
                        db.collection("users").document(uid).set(userMap);
                        startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}