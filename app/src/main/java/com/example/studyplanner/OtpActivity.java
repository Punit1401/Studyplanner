package com.example.studyplanner;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class OtpActivity extends AppCompatActivity {
    EditText otp1, otp2, otp3, otp4;
    TextView resendCode;
    ImageView backBtn;
    int seconds = 30;
    Handler handler = new Handler();
    Runnable runnable;
    private void clearOtpFields(){
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
        otp1.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        backBtn = findViewById(R.id.backBtn);
        TextView timerText = findViewById(R.id.resendCode);
        runnable = new Runnable() {
            @Override
            public void run() {
                if(seconds > 0){
                    timerText.setText("You can resend the code in " + seconds + "s");
                    seconds--;
                    handler.postDelayed(this,1000);
                } else {
                    timerText.setText("Resend Code");
                    timerText.setEnabled(true);
                }
            }
        };
        timerText.setOnClickListener(v -> {
            if(seconds == 0){
                Random random = new Random();
                ForgotPasswordActivity.generatedOtp = String.valueOf(1000 + random.nextInt(9000));
                Toast.makeText(this, "New OTP: " + ForgotPasswordActivity.generatedOtp, Toast.LENGTH_LONG).show();
                seconds = 30;
                handler.post(runnable);
            }
        });
        handler.post(runnable);
        handler.post(runnable);
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp2.requestFocus();
                }
            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp3.requestFocus();
                }
            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otp4.requestFocus();
                }
            }
        });
        otp2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && otp2.getText().length() == 0) {
                otp1.requestFocus();
            }
            return false;
        });
        backBtn.setOnClickListener(v -> finish());
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 1){
                    String enteredOtp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                    if(enteredOtp.equals(ForgotPasswordActivity.generatedOtp)){
                        startActivity(new Intent(OtpActivity.this, NewPasswordActivity.class));
                        finish();
                    }else{
                        Toast.makeText(OtpActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        clearOtpFields();
                    }
                }
            }
        });
    }
}