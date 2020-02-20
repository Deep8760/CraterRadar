package com.example.craterradar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.craterradar.AdminSide.AdminLoginActivity;

public class MainActivity extends AppCompatActivity {
    ImageView Logo,Logo_BG;
    private static int SplashTimeout = 2000;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences =  getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        Logo = findViewById(R.id.logo);
        Logo_BG = findViewById(R.id.logo_bg);
        String userType = sharedPreferences.getString("UserType","");
        if(userType.contentEquals("User"))
        {
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
                finish();
            }
        },SplashTimeout);
            Animation splashScreen = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
            Logo_BG.startAnimation(splashScreen);
            Logo.startAnimation(splashScreen);

        }
        else if(userType.contentEquals("Admin")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, AdminLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            },SplashTimeout);
            Animation splashScreen = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
            Logo_BG.startAnimation(splashScreen);
            Logo.startAnimation(splashScreen);

        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(i);
                    finish();
                }
            },SplashTimeout);
            Animation splashScreen = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
            Logo_BG.startAnimation(splashScreen);
            Logo.startAnimation(splashScreen);
        }


    }
}
