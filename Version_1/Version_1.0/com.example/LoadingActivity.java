package com.example.smartspend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    private static final int LOADING_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assaying_window);

        new Handler().postDelayed(() -> {
            AppPreferences prefs = new AppPreferences(this);
            if (prefs.getName().isEmpty() || prefs.getPurpose().isEmpty() ||
                    prefs.getSum().isEmpty() || prefs.getTerm().isEmpty()) {
                startActivity(new Intent(this, GreetingActivity.class));
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_NAME", prefs.getName());
                intent.putExtra("NEW_GOAL", true); // Флаг для добавления новой цели
                startActivity(intent);
            }
            finish();
        }, LOADING_DELAY);
    }
}