package com.example.smartspend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 секунды задержки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_window);

        new Handler().postDelayed(() -> {
            AppPreferences prefs = new AppPreferences(this);

            if (prefs.isAllDataComplete()) {
                // Данные есть - переходим в MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", prefs.getName());
                startActivity(intent);
            } else {
                // Данных нет - переходим в GreetingActivity
                startActivity(new Intent(this, GreetingActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}