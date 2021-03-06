package com.calljack.im028.calljack.ActivityClasses;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.Session;

public class SplashActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new Session(SplashActivity.this, "").getIsLogin())
                    startActivity(new Intent(SplashActivity.this, ChooseTypeActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
            }
        }, 3000);
    }
}
