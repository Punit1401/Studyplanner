package com.example.studyplanner;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button loginBtn;
    FirebaseAuth auth;
    Dialog loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        loader = new Dialog(this);
        loader.setContentView(R.layout.dialog_loader);
        loader.setCancelable(false);
        ImageView back = findViewById(R.id.btnBack);
        back.setOnClickListener(v -> finish());
        loginBtn.setOnClickListener(v -> loginUser());
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
    private void loginUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        if(userEmail.isEmpty()){
            email.setError("Enter email");
            return;
        }
        if(userPassword.isEmpty()){
            password.setError("Enter password");
            return;
        }
        loader.show();
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(task -> {
                    loader.dismiss();
                    if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null){
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            }
                    }else{
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
                    }
                });
    }
}