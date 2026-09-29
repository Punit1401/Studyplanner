package com.example.studyplanner;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("StudifyPrefs", MODE_PRIVATE);
            boolean firstTime = prefs.getBoolean("firstTime", true);
            if(firstTime){
                startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
            } else {
                if(auth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
                }
            }
            finish();
        }, 2000);
    }
}