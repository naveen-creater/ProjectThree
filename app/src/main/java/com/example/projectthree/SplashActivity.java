package com.example.projectthree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.projectthree.Activity.ContectApiLocation;
import com.example.projectthree.Activity.LocationActivity;
import com.example.projectthree.Activity.SensorActivity;

public class SplashActivity extends AppCompatActivity {
    private ImageView splashMini;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        //initview
        splashMini = findViewById(R.id.base);

        animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.runs);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, ContectApiLocation.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashMini.setAnimation(animation);
    }

}