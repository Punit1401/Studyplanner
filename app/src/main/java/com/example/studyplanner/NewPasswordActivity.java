package com.example.studyplanner;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
public class NewPasswordActivity extends AppCompatActivity {
    EditText pass,confirm;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        pass=findViewById(R.id.newPassword);
        confirm=findViewById(R.id.confirmPassword);
        save=findViewById(R.id.savePasswordBtn);
        save.setOnClickListener(v->{
            String p=pass.getText().toString();
            String c=confirm.getText().toString();
            if(p.length()<6){
                pass.setError("Minimum 6 characters");
                return;
            }
            if(!p.equals(c)){
                confirm.setError("Passwords not match");
                return;
            }
            startActivity(new Intent(NewPasswordActivity.this, PasswordSuccessActivity.class));
        });
    }
}