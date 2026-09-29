package com.example.studyplanner;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordSuccessActivity extends AppCompatActivity {
    Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_success);

        goHome = findViewById(R.id.goHomeBtn);
        goHome.setOnClickListener(v -> {
            Toast.makeText(this, "Password has be changed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(PasswordSuccessActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            startActivity(intent);
            finish();
        });
    }
}