package com.example.studyplanner;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;

public class OnboardingActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    Button btnNext, btnSkip;
    LinearLayout dotsLayout;
    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        dotsLayout = findViewById(R.id.dotsLayout);
        addDotsIndicator(0);
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        OnboardingAdapter adapter = new OnboardingAdapter(this);
        viewPager.setAdapter(adapter);
        btnNext.setOnClickListener(v -> {
            if(viewPager.getCurrentItem() < 1){
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }else{
                finishOnboarding();
            }
        });
        btnSkip.setOnClickListener(v -> finishOnboarding());
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        addDotsIndicator(position);
                    }
                });
    }
    private void finishOnboarding(){
        SharedPreferences prefs = getSharedPreferences("StudifyPrefs",MODE_PRIVATE);
        prefs.edit().putBoolean("firstTime",false).apply();
        startActivity(new Intent(this,SignupActivity.class));
        finish();
    }
    private void addDotsIndicator(int position){
        dots = new TextView[2];
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText("•");
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.teal_700));
        }
    }
}